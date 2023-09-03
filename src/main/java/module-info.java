import uk.iatom.handydisplay.plugin.widget.AbstractWidget;
import uk.iatom.handydisplay.plugin.widget.ClockWidget;

module uk.iatom.handydisplay {
    // Standard library
    requires kotlin.stdlib;
    requires java.desktop;
    // Dependencies
    requires info.picocli;
    requires io.github.classgraph;
    requires hoplite.core;
    requires hoplite.json;
    requires com.fasterxml.jackson.core; // Needed for hoplite-json
    requires kotlin.reflect; // Needed for hoplite-json
    requires java.sql; // Needed for hoplite-json
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires log4j.api.kotlin;

    opens uk.iatom.handydisplay to info.picocli;

    exports uk.iatom.handydisplay;
    exports uk.iatom.handydisplay.plugin;
    exports uk.iatom.handydisplay.gui;
    exports uk.iatom.handydisplay.registry;
    exports uk.iatom.handydisplay.plugin.widget;

    provides AbstractWidget with ClockWidget;
}