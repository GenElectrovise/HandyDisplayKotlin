package uk.iatom.handydisplay.plugins.weather

import uk.iatom.handydisplay.helpers.fileConfig
import uk.iatom.handydisplay.helpers.hdRunFile
import uk.iatom.handydisplay.services.plugin.AbstractPlugin
import java.util.logging.Logger

class WeatherPlugin: AbstractPlugin("weather") {

    val logger = Logger.getLogger(javaClass.name)

    companion object {


        var config: ConfigModel? = null
    }

    init {
        config = fileConfig(
                hdRunFile(
                        this,
                        "weather.properties"
                         )
                           )
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