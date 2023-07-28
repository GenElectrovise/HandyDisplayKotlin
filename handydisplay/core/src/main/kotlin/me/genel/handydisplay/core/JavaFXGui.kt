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
import org.apache.logging.log4j.kotlin.Logging

val WIDTH: Double = 480.0
val HEIGHT: Double = 320.0

class JavaFXGui : Application(), Logging {

    private lateinit var rootStage: Stage
    private lateinit var contentStack: StackPane
    private val order: Set<String>

    var currentWidgetName: StringProperty = SimpleStringProperty("")
        set(value) {
            if (!MOD_MANAGER.mods.containsKey(value.value)) {
                throw NoSuchElementException("currentWidgetName cannot take the value $value because there is no widget associated with the name $value.")
            }
            field = value
        }
    val currentWidget: `AbstractMod`
        get() {
            val name = currentWidgetName.value
            return MOD_MANAGER.mods[name] ?: throw NoSuchElementException("No widget present with the name $name")
        }

    init {
        checkSupported()
        order = createOrder()
        currentWidgetName.value = order.iterator().next()

        currentWidgetName.addListener(ShowNewWidgetOnWidgetNameChangedListener())
    }

    override fun start(primaryStage: Stage?) {
        rootStage = primaryStage ?: throw NullPointerException("Cannot create GUI with null primaryStage!")
        contentStack = StackPane()

        rootStage.scene = Scene(contentStack, WIDTH, HEIGHT)
        rootStage.show()

        val overlay = createOverlayPane(
            { cycleWidgets(false) },
            { cycleWidgets(true) }
        )

        showWidget(currentWidget)
        contentStack.children.add(overlay)
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

    private fun createOrder(): Set<String> {
        val o = setOf("none", "weather")  //TODO Load order from disk
        o.forEach {
            if (!MOD_MANAGER.mods.containsKey(it))
                throw NoSuchElementException("There is no appropriately named widget to bind the name $it to in the given order: $o")
        }
        return o
    }

    fun showWidget(widget: `AbstractMod`) {
        val pane = widget.createContentPane()

        if (contentStack.children.size > 1)
            contentStack.children[0] = pane
        else
            contentStack.children.add(pane)
    }

    fun cycleWidgets(forwards: Boolean) {
        logger.info("Cycling widgets " + (if (forwards) "+/forwards/right" else "-/backwards/left"))

        val oldIndex = order.indexOf(currentWidgetName.value)
        var newIndex = if (forwards) oldIndex + 1 else oldIndex - 1

        if (newIndex < 0) newIndex = order.size - 1
        else if (newIndex >= order.size) newIndex = 0

        currentWidgetName.value = order.elementAt(newIndex)
    }

    inner class ShowNewWidgetOnWidgetNameChangedListener : ChangeListener<String> {
        override fun changed(observable: ObservableValue<out String>?, oldValue: String?, newValue: String?) {
            showWidget(currentWidget)
        }
    }

}