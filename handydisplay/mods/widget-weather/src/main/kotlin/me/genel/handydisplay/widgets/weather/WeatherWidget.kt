package me.genel.handydisplay.widgets.weather

import javafx.fxml.FXMLLoader
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import me.genel.handydisplay.core.AbstractWidget

class WeatherWidget : AbstractWidget("weather", "Weather") {
    override fun createContentPane(): Pane = loadFXML("fxml/weather.fxml", WeatherController())
}