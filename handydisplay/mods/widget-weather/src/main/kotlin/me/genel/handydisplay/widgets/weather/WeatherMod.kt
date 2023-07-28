package me.genel.handydisplay.widgets.weather

import javafx.scene.layout.Pane
import me.genel.handydisplay.core.mod.AbstractMod

class WeatherMod() : AbstractMod("weather", "Weather") {

    override fun finishModLoading() = logger.debug("WeatherMod loading done!")

    override fun createContentPane(): Pane = loadFXML("fxml/weather.fxml", WeatherController())
}