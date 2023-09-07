import uk.iatom.handydisplay.services.plugin.AbstractPlugin;
import uk.iatom.handydisplay.services.plugin.NonePlugin;
import uk.iatom.handydisplay.services.plugin.NoneWidget;
import uk.iatom.handydisplay.services.widget.AbstractWidget;
import uk.iatom.handydisplay.services.widget.ClockWidget;

module iAtom.HandyDisplay {
    // Standard library
    requires kotlin.stdlib;
    requires java.desktop;
    // Dependencies
    requires com.almasb.fxgl.all;
    requires info.picocli;
    requires hoplite.core;
    requires hoplite.json;
    requires com.fasterxml.jackson.core; // Needed for hoplite-json
    requires kotlin.reflect; // Needed for hoplite-json
    requires java.sql; // Needed for hoplite-json
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    opens uk.iatom.handydisplay to info.picocli;

    exports uk.iatom.handydisplay.fxgl;
    exports uk.iatom.handydisplay.helpers;
    exports uk.iatom.handydisplay.registry;
    exports uk.iatom.handydisplay.services.plugin;
    exports uk.iatom.handydisplay.services.widget;

    provides AbstractPlugin with NonePlugin;
    provides AbstractWidget with NoneWidget, ClockWidget;
}