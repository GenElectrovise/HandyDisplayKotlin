package uk.iatom.handydisplay.plugins.weather

import uk.iatom.handydisplay.fileConfig
import uk.iatom.handydisplay.plugin.widget.AbstractWidget
import uk.iatom.handydisplay.hdRunFile
import uk.iatom.handydisplay.plugin.AbstractPlugin
import uk.iatom.handydisplay.registry.Registry

class WeatherPlugin: AbstractPlugin() {


    override val registryName: String = "weather"

    lateinit var config: ConfigModel

    override fun finishPluginLoading() {
        config = fileConfig(
                hdRunFile(
                        this,
                        "weather.properties"
                         )
                           )

        Registry.register<AbstractWidget>(WeatherWidget(config))

        logger.debug("WeatherPlugin loading done!")
    }

    override fun shutdownNow() {
    }

    data class ConfigModel(
            val datetimeFormat: String
                          )
}