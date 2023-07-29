package me.genel.handydisplay.widgets.weather

import javafx.scene.layout.Pane
import me.genel.handydisplay.core.plugin.AbstractWidget

class WeatherWidget: AbstractWidget("weather", "Weather") {

    override fun createContentPane(): Pane = loadFXML("fxml/weather.fxml")
}