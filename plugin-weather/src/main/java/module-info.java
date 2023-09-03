import uk.iatom.handydisplay.plugins.weather.WeatherWidget;
import uk.iatom.handydisplay.services.plugin.AbstractPlugin;
import uk.iatom.handydisplay.plugins.weather.WeatherPlugin;
import uk.iatom.handydisplay.services.widget.AbstractWidget;

module uk.iatom.handydisplay.plugins.weather {
    // Standard library
    requires kotlin.stdlib;
    requires java.desktop;
    requires java.logging;
    requires uk.iatom.handydisplay;
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    provides AbstractPlugin with WeatherPlugin;
    provides AbstractWidget with WeatherWidget;
}