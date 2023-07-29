package me.genel.handydisplay.widgets.weather

import me.genel.handydisplay.core.fileConfig
import me.genel.handydisplay.core.hdRunFile
import me.genel.handydisplay.core.plugin.AbstractPlugin
import me.genel.handydisplay.core.plugin.AbstractWidget
import me.genel.handydisplay.core.register

class WeatherPlugin : AbstractPlugin() {

    override val registryName: String = "weather"

    lateinit var config: ConfigModel

    override fun finishPluginLoading() {
        config = fileConfig(hdRunFile(this, "weather.properties"))

        register<AbstractWidget>(WeatherWidget(config))

        logger.debug("WeatherPlugin loading done!")
    }

    override fun shutdownNow() {
    }

    data class ConfigModel(
        val datetimeFormat: String
    )
}