package me.genel.handydisplay.core

import io.github.classgraph.ClassGraph
import me.genel.handydisplay.core.widget.AbstractWidget
import org.apache.logging.log4j.kotlin.Logging
import java.io.File
import java.io.FileFilter

class ModManager : Logging {

    val widgets: Map<String, AbstractWidget>


    init {
        widgets = collectWidgetInstances()
    }

    private fun collectWidgetInstances(): Map<String, AbstractWidget> {
        val w = ArrayList<AbstractWidget>()

//        val config = createReflectionsConfig()
//        val reflections = Reflections(config)
//        val widgetClasses = reflections.getSubTypesOf(AbstractWidget::class.java)

        val jars = getModJarPaths()
        ClassGraph()
            .enableClassInfo()
            .acceptJars(*jars)
            .acceptPackages(this.javaClass.`package`.name)
            .scan().use { result ->
                val widgetClassInfos = result.getSubclasses(AbstractWidget::class.java)
                val classes = widgetClassInfos.loadClasses()

                logger.info("Found ${widgetClassInfos.size} potential widget${if (widgetClassInfos.size == 1) "" else "s"}!")
                classes.forEach { clazz ->
                    logger.debug("Adding widget: ${clazz.name}")
                    val inst = clazz.getDeclaredConstructor().newInstance()
                    w.add(inst as AbstractWidget)
                }
            }

        logger.info("Loaded widgets: ")
        return w.associateBy {
            logger.info(" + [${it.internalName}]=${it::class.simpleName}")
            it.internalName
        }
    }

    private fun getModJarPaths(): Array<String> {
        val widgetsFile = File("mods")
        if (!widgetsFile.exists()) {
            logger.warn("Mods file ${widgetsFile.absolutePath} doesn't exist - creating...")
            widgetsFile.mkdirs()
        }
        logger.info("Loading mods from ${widgetsFile.absolutePath}")

        return widgetsFile.listFiles(FileFilter { !it.isDirectory })!!.map { it.absolutePath }.toTypedArray()
    }

//    private fun createReflectionsConfig(): Configuration {
//        val files = getModFiles()
//        val urlLoader = URLClassLoader(
//            "mod_url_classloader",
//            files?.map { it.toURI().toURL() }?.toTypedArray()
//                ?: throw NullPointerException("Error collecting files for widget generation: $files"),
//            this.javaClass.classLoader
//        )
//
//        val config = ConfigurationBuilder()
//        val classLoaders = arrayOf(urlLoader, this.javaClass.classLoader)
//        val xxx = ClasspathHelper.forClassLoader(this.javaClass.classLoader)
//        val yyy = ClasspathHelper.forClassLoader(urlLoader)
//        val zzz = ClasspathHelper.forPackage("org")
//        val urls = classLoaders.map { ClasspathHelper.forClassLoader(it) }.flatten()
//
//        logger.debug("Will scans URLs (files) for mods: $urls")
//        logger.debug("Will use ClassLoaders for mods: ${classLoaders.map { it.name }}")
//
//        config.addClassLoaders(*classLoaders)
//        config.addUrls(urls)
//        config.addScanners(Scanners.SubTypes)
//        config.setInputsFilter(
//            FilterBuilder().excludePackage("org.reflections")
//                // JDK Packages
//                // https://docs.oracle.com/en/java/javase/11/docs/api/allpackages-index.html
//                .excludePackage("com.sun")
//                .excludePackage("java")
//                .excludePackage("javax")
//                .excludePackage("jdk")
//                .excludePackage("netscape")
//                .excludePackage("org.ietf")
//                .excludePackage("org.w3c")
//                .excludePackage("org.xml")
//                // Library packages
//                .excludePackage("javafx")
//                .excludePackage("kotlin")
//                .excludePackage("kotlinx")
//                .excludePackage("sun")
//                .excludePackage("info.picocli")
//                .excludePackage("org.jetbrains")
//                .excludePackage("org.slf4j")
//                .excludePackage("org.apache")
//        ) //TODO Store doesn't have anything in :(
//        config.isParallel = true
//
//        return config
//    }
}






