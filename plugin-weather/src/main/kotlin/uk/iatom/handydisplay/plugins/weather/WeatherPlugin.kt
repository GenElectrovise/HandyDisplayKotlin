package uk.iatom.handydisplay.plugins.weather

import uk.iatom.handydisplay.helpers.fileConfig
import uk.iatom.handydisplay.helpers.hdRunFile
import uk.iatom.handydisplay.services.plugin.AbstractPlugin

class WeatherPlugin: AbstractPlugin("weather") {

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
        logger.debug("WeatherPlugin loading done!")
    }

    override fun shutdownNow() {
    }

    data class ConfigModel(
            val datetimeFormat: String
                          )
}