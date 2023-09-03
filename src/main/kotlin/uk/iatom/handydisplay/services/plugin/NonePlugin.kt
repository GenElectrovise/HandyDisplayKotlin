package uk.iatom.handydisplay.services.plugin

import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import org.apache.logging.log4j.kotlin.Logging
import uk.iatom.handydisplay.services.widget.AbstractWidget

class NonePlugin: AbstractPlugin("none"), Logging {


    override fun finishPluginLoading() {
    }

    override fun shutdownNow() {}

    override val registryName: String = "none"
}

class NoneWidget: AbstractWidget(
        "none",
        "None"
                                ) {


    override fun createContentPane(): Pane {
        val box = VBox()

        val label = Label("No widget here :(")
        box.children.add(label)

        return box
    }
}