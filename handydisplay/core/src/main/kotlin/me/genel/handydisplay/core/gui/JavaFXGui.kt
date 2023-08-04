package me.genel.handydisplay.core.gui

import javafx.application.Application
import javafx.application.ConditionalFeature
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import me.genel.handydisplay.core.fileConfig
import me.genel.handydisplay.core.hdRunFile
import me.genel.handydisplay.core.plugin.AbstractPlugin
import me.genel.handydisplay.core.registry.Registry
import org.apache.logging.log4j.kotlin.Logging

const val WIDTH: Double = 480.0
const val HEIGHT: Double = 320.0


// Layers for the GUI stack pane
const val WIDGET_LAYER = 0
const val OVERLAY_LAYER = WIDGET_LAYER + 1


/**
 * JavaFXGui singleton instance.
 */
@Volatile var GUI: JavaFXGui? = null


/**
 * The GUI for HandyDisplay! Handles the displaying of widgets and application lifecycle, plus it
 * delegates to the active mirror.
 *
 * This class is a singleton, the instance of which can be accessed by the package-level `GUI`
 * variable.
 */
class JavaFXGui: Application(), Logging {


    private lateinit var contentStack: StackPane
    private val guiConfig: GuiConfigModel = fileConfig(
            hdRunFile(
                    null,
                    "gui.properties"
                     )
                                                      )

    val currentWidget: SimpleObjectProperty<AbstractWidget> = SimpleObjectProperty()

    init {
        if (GUI != null) throw IllegalStateException("Cannot instantiate multiple GUIs!")
        GUI = this
        checkSupported()
        validateOrder()
    }

    override fun start(primaryStage: Stage?) {
        if (primaryStage == null) throw NullPointerException("Cannot create GUI with null primaryStage!")

        // Set up stack pane (so that I can index its elements by number easily)
        contentStack = StackPane()
        for (i in 0 .. OVERLAY_LAYER) {
            contentStack.children.add(AnchorPane())
        }

        // Configure window/stage
        Platform.setImplicitExit(true)
        primaryStage.setOnCloseRequest { _ ->
            logger.fatal("Close requested - shutting down platform...")
            println("Close requested - shutting down platform...")
            Platform.exit()
        }
        primaryStage.scene = Scene(
                contentStack,
                WIDTH,
                HEIGHT
                                  )
        primaryStage.show()

        // Set up overlay
        val overlay = OverlayCreator.createOverlayPane({ cycleWidgets(false) },
                                                       { cycleWidgets(true) })
        contentStack.children[OVERLAY_LAYER] = overlay

        // Set up initial widget
        currentWidget.addListener(CurrentWidgetPropertyChangedListener())
        currentWidget.value = Registry.get<AbstractWidget>(
                guiConfig.order
                        .iterator()
                        .next()
                                                          )
    }

    override fun stop() {
        super.stop()
        println("Stopping application...")
    }


    /**
     * Log supported JavaFX features.
     */
    private fun checkSupported() {
        logger.debug("Supported JavaFX ConditionalFeatures:")
        ConditionalFeature.entries.forEach {
            try {
                logger.debug(" > ${it.name}: ${Platform.isSupported(it)}")
            } catch (e: Exception) {
                logger.debug(" ! FAILED ${it.name}: ${e.message}")
            }
        }
    }


    /**
     * Ensure that the given widget order (from guiConfig) is valid.
     *
     * @throws IndexOutOfBoundsException If the widget order is empty.
     * @throws NoSuchElementException If a name is present in the order for which there is no
     * AbstractWidget registered.
     */
    private fun validateOrder() {
        if (guiConfig.order.isEmpty()) throw IndexOutOfBoundsException("Cannot start application with an empty widget order. Please configure this in gui.properties. If in doubt, delete gui.properties and a default version will be created in its place.")

        guiConfig.order.forEach {
            if (Registry.get<AbstractPlugin>(it) == null) throw NoSuchElementException("There is no widget named $it to populate the given widget order: ${guiConfig.order}")
        }
    }


    /**
     * Cycle the current widget left/right (+1 / -1) through the order, wrapping to the start/end
     * if needed.
     */
    private fun cycleWidgets(forwards: Boolean) {
        logger.info("Cycling widgets " + (if (forwards) "+/forwards/right" else "-/backwards/left"))

        val oldIndex = guiConfig.order.indexOf(currentWidget.value.registryName)
        var newIndex = if (forwards) oldIndex + 1 else oldIndex - 1

        if (newIndex < 0) newIndex = guiConfig.order.size - 1
        else if (newIndex >= guiConfig.order.size) newIndex = 0

        val newName = guiConfig.order.elementAt(newIndex)
        currentWidget.value = Registry.get<AbstractWidget>(newName)
    }


    /**
     * ChangeListener to update the currently displayed AbstractWidget on the JavaFX stage when
     * the currentWidget property changes.
     */
    inner class CurrentWidgetPropertyChangedListener: ChangeListener<AbstractWidget>, Logging {


        override fun changed(
                observable: ObservableValue<out AbstractWidget>?,
                oldValue: AbstractWidget?,
                newValue: AbstractWidget?
                            ) {
            logger.info("Switching current widget from ${oldValue?.registryName} to ${newValue?.registryName}")
            if (newValue == null) throw NullPointerException("Cannot switch to null widget.")

            oldValue?.onHide()

            val pane = newValue.createContentPane()
            contentStack.children[WIDGET_LAYER] = pane

            newValue.onShow()
        }
    }


    /**
     * Configuration model for the gui.properties file.
     */
    data class GuiConfigModel(
            val order: Set<String>
                             )
}