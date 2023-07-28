package me.genel.handydisplay.core.mod

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import me.genel.handydisplay.core.hdRunFile
import nonapi.io.github.classgraph.utils.JarUtils
import org.apache.logging.log4j.kotlin.Logging
import java.io.File
import java.io.FileFilter
import java.net.URLClassLoader
import kotlin.system.exitProcess

/**
 * Loads mods from disk at application startup and maintains instances of their main classes (any subclass of `AbstractMod`). Order of mod
 * loading cannot be guaranteed.
 */
class ModManager : Logging {

    private val scanResult: ScanResult
    val mods: Map<String, AbstractMod>

    init {
        scanResult = scanClasspathAndJars()
        mods = collectModInstances()
        finishModLoading()
    }

    private fun scanClasspathAndJars(): ScanResult {
        val jars = getModJarPaths()
        logger.info("Found ${jars.size} mod .JAR files:")
        jars.forEach { logger.info(" : ${JarUtils.leafName(it)} - $it") }

        val classLoader = URLClassLoader(
            "modJarURLClassLoader",
            jars.map { File(it).toURI().toURL() }.toTypedArray(),
            javaClass.classLoader
        )

        return ClassGraph().enableClassInfo().addClassLoader(classLoader).scan()
    }

    private fun getModJarPaths(): Array<String> {
        val modsFile = hdRunFile("mods")
        if (!modsFile.exists()) {
            logger.warn("Mods file ${modsFile.absolutePath} doesn't exist - creating...")
            modsFile.mkdirs()
        }
        logger.info("Loading mods from ${modsFile.absolutePath}")

        return modsFile.listFiles(FileFilter { !it.isDirectory })!!.map { it.absolutePath }.toTypedArray()
    }

    private fun collectModInstances(): Map<String, AbstractMod> {
        val subclasses = findModSubclasses()
        val instances = createModInstances(subclasses)
        val map = HashMap<String, AbstractMod>(subclasses.size)

        logger.info("Found widgets:")

        instances.forEach { aw ->
            val old = map.putIfAbsent(aw.internalName, aw)
            if (old != null) throw IllegalStateException("Duplicate widgets of the name ${old.internalName}: ${aw.javaClass.name} & ${old.javaClass.name}")
            logger.info(" + [${aw.internalName}]=${aw::class.simpleName}")
        }

        return map
    }

    private fun findModSubclasses(): List<Class<AbstractMod>> {
        logger.debug("Searching for subclasses of ${AbstractMod::class.qualifiedName}")

        scanResult.use { result ->
            val classInfoList = result.getSubclasses(AbstractMod::class.java)
            @Suppress("UNCHECKED_CAST") return classInfoList.loadClasses() as List<Class<AbstractMod>>
        }
    }

    private fun createModInstances(classes: List<Class<AbstractMod>>) =
        classes.map { clazz -> instantiateModClass(clazz) }

    private fun instantiateModClass(clazz: Class<AbstractMod>): AbstractMod {
        var mie: ModInstantiationException? = null

        try {
            val cons = clazz.getDeclaredConstructor()

            mie = try {
                return cons.newInstance()
            } catch (acc: IllegalAccessException) {
                ModInstantiationException("Found constructor, but unable to access it.", clazz, cons, acc)
            } catch (arg: IllegalArgumentException) {
                ModInstantiationException("Cannot invoke constructor with 0 arguments.", clazz, cons, arg)
            } catch (ins: InstantiationException) {
                ModInstantiationException("newInstance() failed. Unable to instantiate.", clazz, cons, ins)
            } catch (ini: ExceptionInInitializerError) {
                ModInstantiationException("Exception occurred inside constructor.", clazz, cons, ini)
            }

        } catch (nsm: NoSuchMethodException) {
            mie = ModInstantiationException("No public 0-argument constructor found.", clazz, null, nsm)
        } catch (sec: SecurityException) {
            mie = ModInstantiationException("A security manager prohibited searching for the mod constructor.", clazz, null, sec)
        } finally {
            if (mie != null) {
                logger.fatal(mie)
                throw mie
            }
        }

        logger.error("How did I get here?")
        exitProcess(100)
    }

    private fun finishModLoading() {
        mods.forEach {
            logger.info("Finishing loading [${it.key}]")
            it.value.finishModLoading()
        }
    }
}






