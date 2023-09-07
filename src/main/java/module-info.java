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
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens uk.iatom.handydisplay.bootstrap to info.picocli;

    exports uk.iatom.handydisplay.fxgl;
    exports uk.iatom.handydisplay.helpers;
    exports uk.iatom.handydisplay.registry;
    exports uk.iatom.handydisplay.services.plugin;
    exports uk.iatom.handydisplay.services.widget;

    provides AbstractPlugin with NonePlugin;
    provides AbstractWidget with NoneWidget, ClockWidget;

    uses AbstractPlugin;
    uses AbstractWidget;
}