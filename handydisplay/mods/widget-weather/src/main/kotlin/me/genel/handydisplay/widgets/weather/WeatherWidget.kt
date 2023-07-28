package me.genel.handydisplay.widgets.weather

import javafx.scene.layout.Pane
import me.genel.handydisplay.core.`AbstractMod`

class WeatherWidget : `AbstractMod`("weather", "Weather") {
    override fun createContentPane(): Pane = loadFXML("fxml/weather.fxml", WeatherController())
}