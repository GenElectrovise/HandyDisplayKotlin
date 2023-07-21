package me.genel.handydisplay.core

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import me.genel.handydisplay.api.AbstractWidget
import me.genel.handydisplay.api.hdRunFile
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
        val widgetsFile = hdRunFile(null, "mods")
        if (!widgetsFile.exists()) {
            logger.warn("Mods file ${widgetsFile.absolutePath} doesn't exist - creating...")
            widgetsFile.mkdirs()
        }
        logger.info("Loading mods from ${widgetsFile.absolutePath}")

        return widgetsFile.listFiles(FileFilter { !it.isDirectory })!!.map { it.absolutePath }.toTypedArray()
    }

    private inline fun <reified T> findSubclassesOf(): List<T> {
        logger.debug("Searching for subclasses of ${T::class.qualifiedName}")

        val w = ArrayList<T>()

        scanResult.use { result ->
            val widgetClassInfos = result.getSubclasses(T::class.java)
            val classes = widgetClassInfos.loadClasses()

            logger.info("Found ${widgetClassInfos.size} match${if (widgetClassInfos.size == 1) "" else "es"}!")
            classes.forEach { clazz ->
                logger.debug("Instantiating match: ${clazz.name}")
                val inst = clazz.getDeclaredConstructor().newInstance()
                w.add(inst as T)
            }
        }

        return w
    }

    private fun collectWidgetInstances(): Map<String, AbstractWidget> {
        val w = findSubclassesOf<AbstractWidget>()

        logger.info("Found widgets:")
        return w.associateBy {
            logger.info(" + [${it.internalName}]=${it::class.simpleName}")
            it.internalName
        }
    }
}






