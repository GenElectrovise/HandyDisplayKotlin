package uk.iatom.handydisplay.plugins.weather

import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import uk.iatom.handydisplay.plugin.widget.AbstractWidget
import uk.iatom.handydisplay.gui.FXMLHelper
import uk.iatom.handydisplay.gui.FXMLLoadResult

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