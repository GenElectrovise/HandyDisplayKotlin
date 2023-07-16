package me.genel.handydisplay.core

import javafx.application.Application
import javafx.application.ConditionalFeature
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import me.genel.handydisplay.core.widget.AbstractWidget
import org.apache.logging.log4j.kotlin.Logging

val WIDTH: Double = 480.0
val HEIGHT: Double = 320.0

class GUI(val widgetProvider: WidgetManager) : Application(), Logging {

    private lateinit var rootStage: Stage
    private lateinit var contentStack: StackPane

    init {
        checkSupported()
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

        showWidget(widgetProvider.currentWidget)
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

    fun showWidget(widget: AbstractWidget) {
        val pane = widget.createContentPane()

        if (contentStack.children.size > 1)
            contentStack.children[0] = pane
        else
            contentStack.children.add(pane)
    }

    fun cycleWidgets(forwards: Boolean) {
        //TODO Cycle widgets implementation
    }

}