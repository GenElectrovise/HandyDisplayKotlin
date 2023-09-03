package uk.iatom.handydisplay.plugin

import uk.iatom.handydisplay.registry.IRegistrable
import org.apache.logging.log4j.kotlin.Logging

/**
 * The class which the core will search for during plugin loading: children of this class are IRegisterables and will be automatically instantiated
 * and registered.
 *
 * Subclasses should declare a zero-argument constructor.
 */
abstract class AbstractPlugin: IRegistrable<AbstractPlugin>, Logging {


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
}