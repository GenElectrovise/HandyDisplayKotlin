package me.genel.handydisplay.core.mod

import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane
import me.genel.handydisplay.core.hdRunFile
import org.apache.logging.log4j.kotlin.Logging
import java.io.File

/**
 * The class which the core will search for during mod loading: children of this class will be automatically
 * instantiated and stored as singletons by the ModManager, indexed by their internal name.
 *
 * Subclasses should declare a zero-argument constructor.
 *
 * @param internalName How this mod will be referenced internally, such as for setting its position in the widget order. Between 1-32 characters long.
 * @param displayName The name which this mod will display to the user, for example on the widget overlay.
 */
abstract class AbstractMod(val internalName: String, val displayName: String) : Logging {

    init {
        // Internal
        assert(
            internalName.asSequence()
                .all { it.isLetterOrDigit() }) { "Internal widget names may only contain letters and digits. $internalName is invalid." }
        assert(internalName.length in 1..32) { "Internal widget names must have [1,32] characters. $internalName is invalid." }
        // Display
        assert(
            displayName.asSequence()
                .all { it.isLetterOrDigit() }) { "Displayed widget names may only contain letters and digits. $displayName is invalid." }
        assert(displayName.length in 1..16) { "Displayed widget names must have [1,16] characters. $displayName is invalid." }

    }

    /**
     * Called after all mods have been instantiated. The order of calling of this method cannot be guaranteed. This is a good time to check
     * compatibility with other loaded mods.
     */
    abstract fun finishModLoading()

    /**
     * Called when the visible widget switches to that of this mod. Should create a new instance each time, but can achieve data persistence by
     * loading from the disk.
     */
    abstract fun createContentPane(): Pane

    /**
     * Called when mods must shut down immediately because the application wants to close. This must terminate any rogue threads or executors!
     */
    abstract fun shutdownNow()

    //TODO Implement shutdownNow

    /**
     * Convenience method to load a JavaFX pane of type `T` from an FXML file located at the given path within the JAR containing this class. This
     * method avoids nasty issues with ClassLoaders, or JavaFX being unable to find a controller class.
     *
     * This may or may not work if the controller class was loaded by a different ClassLoader. This is dependant on whether the parent ClassLoader
     * will search its siblings/parents/children if it does not find a class. Fortunately, all mod JARs in this app are loaded by the same
     * URLClassLoader, whose parent is the main AppClassLoader (or equivalent in other environments), so controllers from other mods should be
     * perfectly accessible.
     *
     * TODO Check whether controllers within other ClassLoaders can be used.
     */
    fun <T> loadFXML(resourcePath: String): T {
        try {
            val url = this::class.java.classLoader.getResource(resourcePath)
            val loader = FXMLLoader()
            loader.location = url
            loader.classLoader = javaClass.classLoader  // This line is very, very important!!
            return loader.load()
        } catch (cnf: ClassNotFoundException) {
            logger.fatal("Error creating content for widget: $internalName")
            logger.fatal("ClassNotFoundException *may* indicate that a controller was designated in the given FXML.")
            logger.fatal("Remove this designation and try again.")
            logger.fatal(cnf)
            throw cnf
        }
    }

    /**
     * Convenience method to get a file on the disk within the `hdrun/mods/${internalName}` directory. Any time a mod must access a file specific
     * to itself should be done through this method.
     */
    fun modFile(path: String): File = hdRunFile("mods/$internalName/$path")
}