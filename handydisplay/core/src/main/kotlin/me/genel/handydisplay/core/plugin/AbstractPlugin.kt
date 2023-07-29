package me.genel.handydisplay.core.plugin

import me.genel.handydisplay.core.IRegisterable
import me.genel.handydisplay.core.hdRunFile
import org.apache.logging.log4j.kotlin.Logging
import java.io.File

/**
 * The class which the core will search for during plugin loading: children of this class are IRegisterables and will be automatically instantiated
 * and registered.
 *
 * Subclasses should declare a zero-argument constructor.
 */
abstract class AbstractPlugin() : IRegisterable<AbstractPlugin>, Logging {

    /**
     * Called after all plugins have been instantiated. The order of calling of this method cannot be guaranteed. This is a good time to check
     * compatibility with other loaded plugins and register other aspects of this plugin, such as widgets, using `register<R: IRegisterable?
     * (instance)`.
     */
    abstract fun finishPluginLoading()

    /**
     * Called when plugins must shut down immediately because the application wants to close. This must terminate any rogue threads or executors!
     */
    abstract fun shutdownNow()

    //TODO Implement shutdownNow

    /**
     * Convenience method to get a file on the disk within the `hdrun/plugins/${internalName}` directory. Any time a plugin must access a file specific
     * to itself should be done through this method.
     */
    fun pluginFile(path: String): File = hdRunFile("plugins/$registryName/$path")
}