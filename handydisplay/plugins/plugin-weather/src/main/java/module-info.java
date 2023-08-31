module uk.iatom.handydisplay.plugins.weather {
    // Standard library
    requires kotlin.stdlib;
    requires java.desktop;
    requires uk.iatom.handydisplay.core;
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires log4j.api.kotlin;

    provides me.genel.handydisplay.core.plugin.AbstractPlugin with me.genel.handydisplay.plugins.weather.WeatherPlugin;
}