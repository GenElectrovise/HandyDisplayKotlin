package me.genel.handydisplay.core.plugin

import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane
import me.genel.handydisplay.core.IRegisterable
import org.apache.logging.log4j.kotlin.Logging

abstract class AbstractWidget(override val registryName: String, val displayName: String): IRegisterable<AbstractWidget>, Logging {

    init {
        // Display
        assert(
            displayName.asSequence()
                .all { it.isLetterOrDigit() }) { "Displayed widget names may only contain letters and digits. $displayName is invalid." }
        assert(displayName.length in 1..16) { "Displayed widget names must have [1,16] characters. $displayName is invalid." }
    }

    /**
     * Called when the visible widget switches to that of this plugin. Should create a new instance each time, but can achieve data persistence by
     * loading from the disk.
     */
    abstract fun createContentPane(): Pane

    /**
     * Convenience method to load a JavaFX pane of type `T` from an FXML file located at the given path within the JAR containing this class. This
     * method avoids nasty issues with ClassLoaders, or JavaFX being unable to find a controller class.
     *
     * This may or may not work if the controller class was loaded by a different ClassLoader. This is dependant on whether the parent ClassLoader
     * will search its siblings/parents/children if it does not find a class. Fortunately, all plugin JARs in this app are loaded by the same
     * URLClassLoader, whose parent is the main AppClassLoader (or equivalent in other environments), so controllers from other plugins should be
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
            logger.fatal("Error creating content for widget: $registryName")
            logger.fatal("ClassNotFoundException *may* indicate that a controller was designated in the given FXML.")
            logger.fatal("Remove this designation and try again.")
            logger.fatal(cnf)
            throw cnf
        }
    }
}