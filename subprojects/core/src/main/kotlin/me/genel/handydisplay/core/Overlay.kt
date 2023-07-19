package me.genel.handydisplay.core

import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import me.genel.handydisplay.api.AbstractWidget
import org.apache.logging.log4j.kotlin.Logging
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

const val OVERLAY_GROUP = "overlay"

val dateTimeTextFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yy");

private var cycleWidgetsLeft: (() -> Unit)? = null
private var cycleWidgetsRight: (() -> Unit)? = null

fun createOverlayPane(
    left: () -> Unit,
    right: () -> Unit
): Pane {
    cycleWidgetsLeft = left
    cycleWidgetsRight = right

    val url = AbstractWidget::class.java.classLoader.getResource("fxml/overlay.fxml")
    val loader = FXMLLoader(url)
    return loader.load<StackPane>()
}

class OverlayController : Logging {

    inner class WidgetNameChangeListener : ChangeListener<String> {
        override fun changed(observable: ObservableValue<out String>?, oldValue: String?, newValue: String?) =
            triggerNow()

        fun triggerNow() {
            val oldVal = widgetNameText.text
            val newVal = GUI.currentWidget.displayName
            logger.debug("Updating widgetNameText.text from '$oldVal' to '$newVal'...")
            widgetNameText.text = newVal
        }
    }

    @FXML
    lateinit var widgetNameText: Label

    @FXML
    lateinit var datetimeText: Label

    @FXML
    lateinit var lagDataText: Label

    @FXML
    fun initialize() {
        val datetimeTimer = Timer()
        datetimeTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() =
                Platform.runLater { datetimeText.text = LocalDateTime.now().format(dateTimeTextFormatter) }
        }, 0L, 1000L)

        val widgetNameChangeListener = WidgetNameChangeListener()
        GUI.currentWidgetName.addListener(widgetNameChangeListener)
        // widgetNameChangeListener.triggerNow()  //TODO triggerNow() works
        // GUI.instance.widgetManager.currentWidgetName.value = "none" //TODO Does currentWidgetName.value trigger change? (MUST ACTUALLY CHANGE VALUE)
    }

    @FXML
    fun fxmlLeftToggleButtonOnAction() =
        (cycleWidgetsLeft ?: throw NullPointerException("cycleWidgetsLeft lambda has not been set!")).invoke()

    @FXML
    fun fxmlRightToggleButtonOnAction() =
        (cycleWidgetsRight ?: throw NullPointerException("cycleWidgetsRight lambda has not been set!")).invoke()

}