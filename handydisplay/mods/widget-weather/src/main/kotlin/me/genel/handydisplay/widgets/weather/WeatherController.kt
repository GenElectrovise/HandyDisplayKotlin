package me.genel.handydisplay.widgets.weather

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Label
import org.apache.logging.log4j.kotlin.Logging
import java.util.*

class WeatherController : Logging {

    @FXML
    lateinit var label: Label

    @FXML
    fun initialize() {
        val datetimeTimer = Timer()
        datetimeTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() = Platform.runLater { }
        }, 0L, 1000L)
    }

    @FXML
    fun fxmlLeftToggleButtonOnAction() = ""

}