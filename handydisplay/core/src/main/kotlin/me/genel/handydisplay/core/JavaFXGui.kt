package me.genel.handydisplay.core

import javafx.application.Application
import javafx.application.ConditionalFeature
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import me.genel.handydisplay.core.plugin.AbstractPlugin
import me.genel.handydisplay.core.plugin.AbstractWidget
import org.apache.logging.log4j.kotlin.Logging

val WIDTH: Double = 480.0
val HEIGHT: Double = 320.0

class JavaFXGui : Application(), Logging {

    private lateinit var contentStack: StackPane
    private val guiConfig: GuiConfigModel = fileConfig(hdRunFile(null, "gui.properties"))

    var currentWidgetName: StringProperty = SimpleStringProperty("!@£$%^&*()")
        set(value) {
            if (get<AbstractWidget>(value.value) == null) {
                throw NoSuchElementException("currentWidgetName cannot take the value $value because there is no widget associated with the name $value.")
            }
            field = value
        }
    val currentWidget: AbstractWidget
        get() {
            val name = currentWidgetName.value
            return get<AbstractWidget>(name) ?: throw NoSuchElementException("No widget present with the name $name")
        }

    init {
        checkSupported()
        validateOrder()
        currentWidgetName.value = guiConfig.order.iterator().next()

        currentWidgetName.addListener(ShowNewWidgetOnWidgetNameChangedListener())
    }

    override fun start(primaryStage: Stage?) {
        if (primaryStage == null) throw NullPointerException("Cannot create GUI with null primaryStage!")

        contentStack = StackPane()

        Platform.setImplicitExit(true)
        primaryStage.setOnCloseRequest { _ ->
            logger.fatal("Close requested, shutting down platform...")
            println("Close requested, shutting down platform...")
            Platform.exit()
        }

        primaryStage.scene = Scene(contentStack, WIDTH, HEIGHT)
        primaryStage.show()

        val overlay = createOverlayPane(
            { cycleWidgets(false) },
            { cycleWidgets(true) }
        )

        showWidget(currentWidget)
        contentStack.children.add(overlay)
    }

    override fun stop() {
        super.stop()
    }

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

    private fun validateOrder() {
        guiConfig.order.forEach {
            if (get<AbstractPlugin>(it) == null)
                throw NoSuchElementException("There is no appropriately named widget to bind the name $it to in the given order: ${guiConfig.order}")
        }
    }

    fun showWidget(widget: AbstractWidget) {
        val pane = widget.createContentPane()

        if (contentStack.children.size > 1)
            contentStack.children[0] = pane
        else
            contentStack.children.add(pane)
    }

    fun cycleWidgets(forwards: Boolean) {
        logger.info("Cycling widgets " + (if (forwards) "+/forwards/right" else "-/backwards/left"))

        val oldIndex = guiConfig.order.indexOf(currentWidgetName.value)
        var newIndex = if (forwards) oldIndex + 1 else oldIndex - 1

        if (newIndex < 0) newIndex = guiConfig.order.size - 1
        else if (newIndex >= guiConfig.order.size) newIndex = 0

        currentWidgetName.value = guiConfig.order.elementAt(newIndex)
    }

    inner class ShowNewWidgetOnWidgetNameChangedListener : ChangeListener<String> {
        override fun changed(observable: ObservableValue<out String>?, oldValue: String?, newValue: String?) {
            showWidget(currentWidget)
        }
    }

    data class GuiConfigModel(
        val order: Set<String>
    )

}