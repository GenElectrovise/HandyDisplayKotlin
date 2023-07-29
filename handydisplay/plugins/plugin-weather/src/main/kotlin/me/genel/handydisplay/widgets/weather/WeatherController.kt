package me.genel.handydisplay.widgets.weather

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.layout.HBox
import org.apache.logging.log4j.kotlin.Logging
import java.util.*

class WeatherController : Logging {

    var config: WeatherPlugin.ConfigModel? = null

    @FXML
    lateinit var containerHBox: HBox

    @FXML
    fun initialize() {
        try {
            logger.debug("Starting weather controller...")

            val datetimeTimer = Timer()
            datetimeTimer.scheduleAtFixedRate(object : TimerTask() { //TODO dispose of weather executor on program closing
                override fun run() = Platform.runLater {
                    println("Not updating datetime yet :(")
                }
            }, 0L, 1000L)
        } catch (ex: Exception) {
            throw ex
        }
    }
}