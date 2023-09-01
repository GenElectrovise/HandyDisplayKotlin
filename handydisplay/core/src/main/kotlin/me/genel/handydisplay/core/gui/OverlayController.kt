package me.genel.handydisplay.core.gui

import javafx.application.Platform
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
    }
}