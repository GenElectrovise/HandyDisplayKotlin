package me.genel.handydisplay.widgets.weather

import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import me.genel.handydisplay.api.AbstractWidget

class WeatherWidget: AbstractWidget("weather", "Weather") {
    override fun createContentPane(): Pane {
        return AnchorPane()
    }
}