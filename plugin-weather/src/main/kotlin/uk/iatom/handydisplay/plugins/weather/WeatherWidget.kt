package uk.iatom.handydisplay.plugins.weather

import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import uk.iatom.handydisplay.services.widget.AbstractWidget
import uk.iatom.handydisplay.services.widget.FXMLHelper

class WeatherWidget: AbstractWidget(
        "weather",
        "Weather"
                                   ) {


    private lateinit var loadResult: FXMLHelper.LoadResult<WeatherController, HBox>

    override fun createContentPane(): Pane {
        loadResult = FXMLHelper.loadFXML("fxml/weather.fxml")
        loadResult.controller.config = WeatherPlugin.config
        return loadResult.rootComponent
    }
}