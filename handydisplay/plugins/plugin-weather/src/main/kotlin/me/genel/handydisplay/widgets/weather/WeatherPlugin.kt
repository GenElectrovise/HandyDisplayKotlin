package me.genel.handydisplay.widgets.weather

import javafx.scene.layout.Pane
import me.genel.handydisplay.core.plugin.AbstractPlugin

class WeatherPlugin : AbstractPlugin("Weather") {

    override val registryName: String = "weather"

    lateinit var controller: WeatherController

    override fun finishPluginLoading() = logger.debug("WeatherMod loading done!")

    override fun createContentPane(): Pane = loadFXML("fxml/weather.fxml")

    override fun shutdownNow() {
    }

}