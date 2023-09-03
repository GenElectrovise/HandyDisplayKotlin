import uk.iatom.handydisplay.plugins.weather.WeatherWidget;
import uk.iatom.handydisplay.services.plugin.AbstractPlugin;
import uk.iatom.handydisplay.plugins.weather.WeatherPlugin;
import uk.iatom.handydisplay.services.widget.AbstractWidget;

module uk.iatom.handydisplay.plugins.weather {
    // Standard library
    requires kotlin.stdlib;
    requires java.desktop;
    requires uk.iatom.handydisplay;
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires log4j.api.kotlin;

    provides AbstractPlugin with WeatherPlugin;
    provides AbstractWidget with WeatherWidget;
}