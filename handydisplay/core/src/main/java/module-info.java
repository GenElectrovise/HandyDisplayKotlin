import me.genel.handydisplay.core.plugin.widget.AbstractWidget;

module uk.iatom.handydisplay.core {
    // Standard library
    requires kotlin.stdlib;
    requires java.desktop;
    // Dependencies
    requires info.picocli;
    requires io.github.classgraph;
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires log4j.api.kotlin;

    opens me.genel.handydisplay.core to info.picocli;

    exports me.genel.handydisplay.core;
    exports me.genel.handydisplay.core.plugin;
    exports me.genel.handydisplay.core.gui;
    exports me.genel.handydisplay.core.registry;
    exports me.genel.handydisplay.core.plugin.widget;

    provides AbstractWidget with me.genel.handydisplay.core.plugin.widget.ClockWidget;
}