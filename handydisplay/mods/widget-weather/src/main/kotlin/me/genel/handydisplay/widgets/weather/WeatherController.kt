package me.genel.handydisplay.widgets.weather

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.stage.WindowEvent
import org.apache.logging.log4j.kotlin.Logging
import java.util.*

class WeatherController : Logging {

    @FXML
    lateinit var containerHBox: HBox

    @FXML
    fun initialize() {
        containerHBox.scene.window.addEventFilter(WindowEvent.WINDOW_HIDING) { onHide() }

        val datetimeTimer = Timer()
        datetimeTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() = Platform.runLater { }
        }, 0L, 1000L)
    }

    private fun onHide() {
        logger.fatal("WEATHER CONTROL WINDOW HIDING")
    }

    @FXML
    fun fxmlLeftToggleButtonOnAction() = ""

}