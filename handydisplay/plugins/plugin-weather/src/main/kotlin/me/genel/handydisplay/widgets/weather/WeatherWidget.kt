package me.genel.handydisplay.widgets.weather

import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import me.genel.handydisplay.core.plugin.AbstractWidget

class WeatherWidget(private val config: WeatherPlugin.ConfigModel): AbstractWidget(
        "weather",
        "Weather"
                                                                                  ) {


    private lateinit var loadResult: FXMLLoadResult<WeatherController, HBox>

    override fun createContentPane(): Pane {
        loadResult = loadFXML("fxml/weather.fxml")
        loadResult.controller.config = config
        return loadResult.rootComponent
    }
}