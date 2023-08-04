package me.genel.handydisplay.core.gui

import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.control.Label
import org.apache.logging.log4j.kotlin.Logging
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * FXML controller for the JavaFXGui overlay.
 */
class OverlayController: Logging {


    val dateTimeTextFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yy")

    var leftToggleAction: (() -> Unit)? = null
    var rightToggleAction: (() -> Unit)? = null

    inner class UpdateTitleWhenWidgetNameChangedListener: ChangeListener<AbstractWidget> {


        override fun changed(
                observable: ObservableValue<out AbstractWidget>?,
                oldValue: AbstractWidget?,
                newValue: AbstractWidget?
                            ) {
            val oldVal = widgetNameText.text
            val newVal = newValue?.displayName
            logger.debug("Updating widgetNameText.text from '$oldVal' to '$newVal'...")
            widgetNameText.text = newVal
        }
    }


    @FXML lateinit var widgetNameText: Label


    @FXML lateinit var datetimeText: Label


    @FXML lateinit var lagDataText: Label


    @FXML fun initialize() {
        val datetimeTimer = Timer()
        datetimeTimer.scheduleAtFixedRate(
                object: TimerTask() {
                    override fun run() = Platform.runLater {
                        datetimeText.text = LocalDateTime
                                .now()
                                .format(dateTimeTextFormatter)
                    }
                },
                0L,
                1000L
                                         )

        val widgetNameChangeListener = UpdateTitleWhenWidgetNameChangedListener()

        (GUI?.currentWidget
                ?: throw IllegalStateException("GUI cannot be null at overlay construction.")).addListener(widgetNameChangeListener)
    }


    @FXML fun fxmlLeftToggleButtonOnAction() = (leftToggleAction
            ?: throw NullPointerException("cycleWidgetsLeft lambda has not been set!")).invoke()


    @FXML fun fxmlRightToggleButtonOnAction() = (rightToggleAction
            ?: throw NullPointerException("cycleWidgetsRight lambda has not been set!")).invoke()
}