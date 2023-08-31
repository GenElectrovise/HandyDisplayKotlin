package me.genel.handydisplay.core.plugin

import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import me.genel.handydisplay.core.plugin.widget.AbstractWidget
import me.genel.handydisplay.core.registry.Registry
import org.apache.logging.log4j.kotlin.Logging

class NonePlugin: AbstractPlugin(), Logging {


    override fun finishPluginLoading() {
        Registry.register<AbstractWidget>(NoneWidget())
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