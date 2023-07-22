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
    val widgets: Map<String, AbstractWidget>

    init {
        scanResult = scanClasspathAndJars()
        widgets = collectWidgetInstances()
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

    private inline fun <reified T> findSubclassesOf(): List<T> {
        logger.debug("Searching for subclasses of ${T::class.qualifiedName}")

        val w = ArrayList<T>()

        scanResult.use { result ->
            val classInfos = result.getSubclasses(T::class.java)
            val classes = classInfos.loadClasses()

            logger.info("Found ${classInfos.size} match${if (classInfos.size == 1) "" else "es"}!")
            classes.forEach { clazz ->
                logger.debug("Instantiating match: ${clazz.name}")
                val inst = clazz.getDeclaredConstructor().newInstance()
                w.add(inst as T)
            }
        }

        return w
    }

    private fun collectWidgetInstances(): Map<String, AbstractWidget> {
        val list = findSubclassesOf<AbstractWidget>()
        val map = HashMap<String, AbstractWidget>(list.size)

        logger.info("Found widgets:")

        list.forEach { aw ->
            val old = map.putIfAbsent(aw.internalName, aw)
            if (old != null) throw IllegalStateException("Duplicate widgets of the name ${old.internalName}: ${aw.javaClass.name} & ${old.javaClass.name}")
            logger.info(" + [${aw.internalName}]=${aw::class.simpleName}")
        }

        return map
    }
}






