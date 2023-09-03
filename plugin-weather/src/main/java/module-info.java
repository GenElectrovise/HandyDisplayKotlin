import uk.iatom.handydisplay.plugin.AbstractPlugin;
import uk.iatom.handydisplay.plugins.weather.WeatherPlugin;

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
}