package me.genel.handydisplay.core

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import nonapi.io.github.classgraph.utils.JarUtils
import org.apache.logging.log4j.kotlin.Logging
import java.io.File
import java.io.FileFilter
import java.net.URLClassLoader

class ModManager : Logging {

    private val scanResult: ScanResult
    val mods: Map<String, AbstractMod>

    init {
        scanResult = scanClasspathAndJars()
        mods = collectModInstances()
    }

    private fun scanClasspathAndJars(): ScanResult {
        val jars = getModJarPaths()
        logger.info("Found ${jars.size} mod .JAR files:")
        jars.forEach { logger.info(" : ${JarUtils.leafName(it)} - $it") }

        val classLoader = URLClassLoader(jars.map { File(it).toURI().toURL() }.toTypedArray())

        return ClassGraph()
            .enableClassInfo()
            .addClassLoader(classLoader)
            .scan()
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
            return classInfoList.loadClasses() as List<Class<AbstractMod>>//TODO Unchecked abstractmod class cast
        }
    }

    private fun createModInstances(classes: List<Class<AbstractMod>>) =
        classes.map { clazz -> instantiateModClass(clazz) }

    private fun instantiateModClass(clazz: Class<AbstractMod>): AbstractMod {
        try {
            return clazz.getDeclaredConstructor().newInstance()
        } catch (nsm: NoSuchMethodError) {
            throw nsm
        } catch (se: SecurityException) {
            throw se
        }
    }
}






