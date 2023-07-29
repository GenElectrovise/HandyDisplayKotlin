package me.genel.handydisplay.core.plugin

import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import me.genel.handydisplay.core.register
import org.apache.logging.log4j.kotlin.Logging

class NonePlugin : AbstractPlugin(), Logging {
    override fun finishPluginLoading() {
        register<AbstractWidget>(NoneWidget())
    }

    override fun shutdownNow() {}

    override val registryName: String = "none"
}

class NoneWidget: AbstractWidget("none", "None") {

    override fun createContentPane(): Pane {
        val box = VBox()

        val label = Label("No widget here :(")
        box.children.add(label)

        return box
    }
}