package uk.iatom.handydisplay.services.widget

import javafx.scene.layout.Pane
import uk.iatom.handydisplay.registry.IRegistrable
import org.apache.logging.log4j.kotlin.Logging

/**
 * A screen which can be displayed to the user. Must be registered and its registryName added to
 * the JavaFxGui order to function. Please note that technically *this* class is not actually
 * displayed because it acts as a supplier for a JavaFX Pane. See methods for details.
 *
 * @param registryName The name of this widget will be accessed by when it is registered.
 * @param displayName The name of this widget to be displayed by the JavaFXGui overlay when this
 * AbstractWidget is visible.
 *
 * @see IRegistrable
 */
abstract class AbstractWidget(
        override val registryName: String,
        val displayName: String
                             ): IRegistrable<AbstractWidget>, Logging {


    init { // Display
        assert(displayName
                       .asSequence()
                       .all { it.isLetterOrDigit() }) { "Displayed widget names may only contain letters and digits. $displayName is invalid." }
        assert(displayName.length in 1 .. 16) { "Displayed widget names must have [1,16] characters. $displayName is invalid." }
    }


    /**
     * Called when the visible widget switches to that of this plugin. Should create a new instance each time, but can achieve data persistence by
     * loading from the disk. To perform logic at creation or close time, it is recommended to use `@FXML` annotations (such as on an `initialize()`
     * method) in a JavaFX controller.
     */
    abstract fun createContentPane(): Pane


    /**
     * Called when the HandyDisplay GUI switches the current widget AWAY FROM this widget.
     *
     * WARNING: THIS METHOD IS CALLED ON THE MAIN THREAD, NOT THE JAVAFX THREAD. This means that GUI elements cannot be updated from this thread,
     * unless `Platform.runLater()` is used.
     *
     * Can be used to notify JavaFX controllers that they should cancel background tasks, so long as care with multithreading is taken.
     */
    fun onHide() {
        logger.debug("Hiding $registryName")
    }


    /**
     * Called when the HandyDisplay GUI switches the current widget ONTO this widget.
     *
     * WARNING: THIS METHOD IS CALLED ON THE MAIN THREAD, NOT THE JAVAFX THREAD. This means that GUI elements cannot be updated from this thread,
     * unless `Platform.runLater()` is used.
     *
     * This is called after `createContentPane(): Pane`, so a controller may be available if the widget author has made one, but BE CAREFUL. It is
     * preferable to use the `@FXML` annotation with an `initialize()` method for configuring a controller.
     */
    fun onShow() {
        logger.debug("Showing $registryName")
    }
}