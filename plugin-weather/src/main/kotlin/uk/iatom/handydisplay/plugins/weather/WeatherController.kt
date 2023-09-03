package uk.iatom.handydisplay.plugins.weather

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.layout.HBox
import java.util.*
import java.util.logging.*

class WeatherController {


    var logger = Logger.getLogger(javaClass.name)
    var config: WeatherPlugin.ConfigModel? = null


    @FXML lateinit var containerHBox: HBox

    private val datetimeTimer = Timer()


    @FXML fun initialize() {
        logger.fine("Initializing weather controller...")

        // When the root node has no parent (has been removed from the scene tree),
        // the widget has been hidden and can be disposed of.
        containerHBox
                .parentProperty()
                .addListener { _, _, new ->
                    if (new == null) {
                        shutdown()
                    }
                }

        try {
            datetimeTimer.scheduleAtFixedRate(
                    object: TimerTask() { //TODO dispose of weather executor on program closing
                        override fun run() = Platform.runLater {
                            logger.finer("Not updating datetime yet :(")
                        }
                    },
                    0L,
                    1000L
                                             )
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun shutdown() {
        logger.fine("Shutting down weather controller...")
        datetimeTimer.cancel()
        datetimeTimer.purge()
    }
}
