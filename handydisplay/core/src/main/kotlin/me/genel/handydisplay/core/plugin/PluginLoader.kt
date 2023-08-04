package me.genel.handydisplay.core.plugin

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import me.genel.handydisplay.core.hdRunFile
import me.genel.handydisplay.core.registry.Registry
import nonapi.io.github.classgraph.utils.JarUtils
import org.apache.logging.log4j.kotlin.Logging
import java.io.File
import java.io.FileFilter
import java.net.URLClassLoader
import kotlin.system.exitProcess


/**
 * Loads plugins from disk at application startup and maintains instances of their main classes (any subclass of `AbstractPlugin`). Order of plugin
 * loading cannot be guaranteed.
 */
object PluginLoader: Logging {


    /**
     * The cached result of the ClassGraph scan.
     */
    private val scanResult: ScanResult


    /**
     * _Three classes for the BootstrapClassLoader under the JVM,_
     *
     * _Seven for the AppClassLoaders in their halls of JARs,_
     *
     * _Nine for URLClassLoaders, doomed to be garbage collected,_
     *
     * _One for the Master Class Loader in the Plugin Loader,_
     *
     * _In the Handy Display, where the Kotlin lies,_
     *
     * _One ClassLoader to rule them all,_
     *
     * _One ClassLoader to find them,_
     *
     * _One ClassLoader to bring them all and in the PluginLoader bind them,_
     *
     * _In the Handy Display, where the Kotlin lies._
     */
    lateinit var masterClassLoader: ClassLoader

    init {
        scanResult = scanClasspathAndJars()
        val plugins = collectPluginInstances()
        Registry.registerAll<AbstractPlugin>(plugins)
        finishPluginLoading()
    }


    /**
     * Locate JAR files in the runtime plugin directory, loads their classes and creates a
     * ScanResult.
     *
     * @return The ScanResult
     */
    private fun scanClasspathAndJars(): ScanResult {
        val jars = getPluginJarPaths()
        logger.info("Found ${jars.size} plugin .JAR files:")
        jars.forEach { logger.info(" : ${JarUtils.leafName(it)} - $it") }

        masterClassLoader = URLClassLoader("pluginJarURLClassLoader",
                                           jars
                                                   .map {
                                                       File(it)
                                                               .toURI()
                                                               .toURL()
                                                   }
                                                   .toTypedArray(),
                                           javaClass.classLoader)

        return ClassGraph()
                .enableClassInfo()
                .addClassLoader(masterClassLoader)
                .scan()
    }


    /**
     * @return A list of the absolute paths to the plugin JAR files.
     */
    private fun getPluginJarPaths(): Array<String> {
        val pluginsFile = hdRunFile(
                null,
                "plugins"
                                   )
        if (!pluginsFile.exists()) {
            logger.warn("Plugins file ${pluginsFile.absolutePath} doesn't exist - creating...")
            pluginsFile.mkdirs()
        }
        logger.info("Loading plugins from ${pluginsFile.absolutePath}")

        return pluginsFile
                .listFiles(FileFilter { !it.isDirectory })!!
                .map { it.absolutePath }
                .toTypedArray()
    }


    /**
     * @return A Set of instances of the detected plugins.
     */
    private fun collectPluginInstances(): Set<AbstractPlugin> {
        val subclasses = findPluginSubclasses()
        val instances = createPluginInstances(subclasses)
        val set = mutableSetOf<AbstractPlugin>()

        logger.info("Found plugins:")

        instances.forEach { ap ->
            if (!set.add(ap)) {
                val duplicate = set.first { it == ap }
                throw IllegalStateException("Duplicate plugins of the name ${ap.registryName}: ${ap.javaClass.name} & ${duplicate.javaClass.name}")
            }
            logger.info(" + [${ap.registryName}]=${ap::class.simpleName}")
        }

        return set
    }


    /**
     * Use the cached ScanResult to find subclasses of AbstractPlugin in the combined ClassLoader
     * for the main app and the JAR files.
     *
     * @return A list of all the detected classes extending from AbstractPlugin.
     */
    private fun findPluginSubclasses(): List<Class<AbstractPlugin>> {
        logger.debug("Searching for subclasses of ${AbstractPlugin::class.qualifiedName}")

        scanResult.use { result ->
            val classInfoList = result.getSubclasses(AbstractPlugin::class.java)
            @Suppress("UNCHECKED_CAST") return classInfoList.loadClasses() as List<Class<AbstractPlugin>>
        }
    }


    /**
     * Creates an instance of every class (extending AbstractPlugin) which is passed to it.
     *
     * @param classes The list of classes to instantiate.
     */
    private fun createPluginInstances(classes: List<Class<AbstractPlugin>>) =
            classes.map { clazz -> instantiatePluginClass(clazz) }


    /**
     * Instantiates the given clazz, with lots of error handling!!
     *
     * @param clazz The class, extending AbstractPlugin, to instantiate.
     *
     * @throws PluginInstantiationException For any 'normal' error, otherwise fails fatally.
     *
     * @return A new instance of clazz.
     */
    private fun instantiatePluginClass(clazz: Class<AbstractPlugin>): AbstractPlugin {
        var mie: PluginInstantiationException? = null

        try {
            val cons = clazz.getDeclaredConstructor()

            mie = try {
                return cons.newInstance()
            } catch (acc: IllegalAccessException) {
                PluginInstantiationException(
                        "Found constructor, but unable to access it.",
                        clazz,
                        cons,
                        acc
                                            )
            } catch (arg: IllegalArgumentException) {
                PluginInstantiationException(
                        "Cannot invoke constructor with 0 arguments.",
                        clazz,
                        cons,
                        arg
                                            )
            } catch (ins: InstantiationException) {
                PluginInstantiationException(
                        "newInstance() failed. Unable to instantiate.",
                        clazz,
                        cons,
                        ins
                                            )
            } catch (ini: ExceptionInInitializerError) {
                PluginInstantiationException(
                        "Exception occurred inside constructor.",
                        clazz,
                        cons,
                        ini
                                            )
            }
        } catch (nsm: NoSuchMethodException) {
            mie = PluginInstantiationException(
                    "No public 0-argument constructor found.",
                    clazz,
                    null,
                    nsm
                                              )
        } catch (sec: SecurityException) {
            mie = PluginInstantiationException(
                    "A security manager prohibited searching for the plugin constructor.",
                    clazz,
                    null,
                    sec
                                              )
        } finally {
            if (mie != null) {
                logger.fatal(mie)
                throw mie
            }
        }

        logger.error("How did I get here?")
        exitProcess(100)
    }


    /**
     * Called after plugins have been instantiated. Finishes loading each plugin, in no
     * particular order.
     */
    private fun finishPluginLoading() {

        val plugins = Registry.getAll<AbstractPlugin>()
                ?: throw NullPointerException("Unable to getAll<AbstractPlugin> for finishPluginLoading")

        plugins.forEach {
            logger.info("Finishing loading [${it.registryName}]")
            it.finishPluginLoading()
        }
    }
}






