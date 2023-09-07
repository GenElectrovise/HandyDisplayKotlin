package uk.iatom.handydisplay.plugins.weather

import com.almasb.fxgl.input.Input
import uk.iatom.handydisplay.fxgl.HDApp
import uk.iatom.handydisplay.helpers.hdRunFile
import uk.iatom.handydisplay.helpers.propertiesConfig
import uk.iatom.handydisplay.services.plugin.AbstractPlugin
import java.util.logging.*

class WeatherPlugin: AbstractPlugin("weather") {


    private val logger: Logger = Logger.getLogger(javaClass.name)

    companion object {


        var config: ConfigModel? = null
    }

    init {
        config = propertiesConfig(
                hdRunFile(
                        this,
                        "weather.properties"
                         )
                           )
    }

    override fun onInitInput(input: Input) {
        logger.info("Initialising WeatherPlugin inputs...")
    }

    override fun onInitGame(hdApp: HDApp) {
        logger.info("Initialising WeatherPlugin game inputs...")
    }

    override fun finishPluginLoading() {
        logger.fine("WeatherPlugin loading done!")
    }

    override fun shutdownNow() {
    }

    data class ConfigModel(
            val datetimeFormat: String
                          )
}