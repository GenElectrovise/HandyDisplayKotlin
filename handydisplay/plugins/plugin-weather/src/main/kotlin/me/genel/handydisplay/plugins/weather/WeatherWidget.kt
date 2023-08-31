package me.genel.handydisplay.plugins.weather

import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import me.genel.handydisplay.core.gui.AbstractWidget
import me.genel.handydisplay.core.gui.FXMLHelper
import me.genel.handydisplay.core.gui.FXMLLoadResult

class WeatherWidget(private val config: WeatherPlugin.ConfigModel): AbstractWidget(
        "weather",
        "Weather"
                                                                                  ) {


    private lateinit var loadResult: FXMLLoadResult<WeatherController, HBox>

    override fun createContentPane(): Pane {
        loadResult = FXMLHelper.loadFXML("fxml/weather.fxml")
        loadResult.controller.config = config
        return loadResult.rootComponent
    }
}