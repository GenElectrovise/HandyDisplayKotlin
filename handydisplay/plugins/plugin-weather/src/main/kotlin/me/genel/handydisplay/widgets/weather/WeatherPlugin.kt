package me.genel.handydisplay.widgets.weather

import me.genel.handydisplay.core.plugin.AbstractPlugin
import me.genel.handydisplay.core.plugin.AbstractWidget
import me.genel.handydisplay.core.register

class WeatherPlugin : AbstractPlugin() {

    override val registryName: String = "weather"

    lateinit var controller: WeatherController

    override fun finishPluginLoading() {
        register<AbstractWidget>(WeatherWidget())
        logger.debug("WeatherPlugin loading done!")
    }

    override fun shutdownNow() {
    }

}