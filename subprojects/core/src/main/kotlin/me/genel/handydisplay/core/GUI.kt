package me.genel.handydisplay.core

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import me.genel.handydisplay.core.widget.AbstractWidget
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger

val WIDTH: Double = 480.0
val HEIGHT: Double = 320.0

class GUI(val widgetProvider: WidgetManager) : Application(), Logging {

    var contentScene: Scene
        get() = contentStage?.scene ?: throw NullPointerException("contentStage or contentStage.scene is null. Perhaps it was accessed before assignment?")
        set(value) = if (contentStage != null) contentStage!!.scene = value else throw NullPointerException("contentStage is null so a stage cannot be assigned to it. Perhaps it was accessed before assignment?")

    var contentStage: Stage? = null
    var overlayScene: Scene?
        get() = overlayStage?.scene ?: throw NullPointerException("overlayStage or overlayStage.scene is null. Perhaps it was accessed before assignment?")
        set(value) = if (overlayStage != null) overlayStage!!.scene = value else throw NullPointerException("overlayStage is null so a stage cannot be assigned to it. Perhaps it was accessed before assignment?")
    var overlayStage: Stage? = null

    override fun start(primaryStage: Stage?) {
        assert(primaryStage != null)
        contentStage = primaryStage

        showWidget(widgetProvider.currentWidget)

        logger.debug("Transparency supported: " + Platform.isSupported(javafx.application.ConditionalFeature.TRANSPARENT_WINDOW))
        overlayStage = Stage(StageStyle.TRANSPARENT)
        overlayStage!!.initOwner(contentStage)
        overlayStage!!.initModality(Modality.NONE)
        this.overlayScene = Scene(createOverlayPane(
            {cycleWidgets(false)},
            {cycleWidgets(true)}
        ), WIDTH, HEIGHT, Color.TRANSPARENT)

        contentStage!!.show()
        overlayStage!!.show()
    }

    fun showWidget(widget: AbstractWidget) {
        val pane = widget.createContentPane()
        contentScene = Scene(pane, WIDTH, HEIGHT)
    }

    fun cycleWidgets(forwards: Boolean) {

    }

}