import uk.iatom.handydisplay.plugins.weather.WeatherWidget;
import uk.iatom.handydisplay.services.plugin.AbstractPlugin;
import uk.iatom.handydisplay.plugins.weather.WeatherPlugin;
import uk.iatom.handydisplay.services.widget.AbstractWidget;

module iAtom.HandyDisplay.Plugins.Weather {
    // Standard library
    requires kotlin.stdlib;
    requires java.desktop;
    requires java.logging;
    requires iAtom.HandyDisplay;
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires com.almasb.fxgl.core;

    provides AbstractPlugin with WeatherPlugin;
    provides AbstractWidget with WeatherWidget;
}