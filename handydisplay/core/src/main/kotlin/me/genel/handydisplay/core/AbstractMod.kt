package me.genel.handydisplay.core

import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane
import org.apache.logging.log4j.kotlin.Logging
import java.io.File

/**
 * The class which the core will search for during mod loading: children of this class will be automatically
 * instantiated and stored as singletons by the ModManager, indexed by their internal name.
 *
 * Subclasses should declare a zero-argument constructor.
 */
abstract class AbstractMod(val internalName: String, val displayName: String) : Logging {

    init {
        // Internal
        assert(
            internalName.asSequence()
                .all { it.isLetterOrDigit() }) { "Internal widget names may only contain letters and digits. $internalName is invalid." }
        assert(internalName.length in 1..32) { "Internal widget names must have (0,32] characters. $internalName is invalid." }
        // Display
        assert(
            displayName.asSequence()
                .all { it.isLetterOrDigit() }) { "Displayed widget names may only contain letters and digits. $displayName is invalid." }
        assert(displayName.length in 1..16) { "Displayed widget names must have (0,16] characters. $displayName is invalid." }

    }

    abstract fun createContentPane(): Pane

    fun <T> loadFXML(resourcePath: String, controller: Any): T {
        try {
            val url = this::class.java.classLoader.getResource(resourcePath)
            val loader = FXMLLoader(url, null, null) { _ -> controller }
            return loader.load()
        } catch (cnf: ClassNotFoundException) {
            logger.fatal("Error creating content for widget: $internalName")
            logger.fatal("ClassNotFoundException *may* indicate that a controller was designated in the given FXML.")
            logger.fatal("Remove this designation and try again.")
            logger.fatal(cnf)
            throw cnf
        }
    }

    fun widgetFile(path: String): File = hdRunFile("widgets/$internalName/$path")
}