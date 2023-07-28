package me.genel.handydisplay.core.mod

import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import org.apache.logging.log4j.kotlin.Logging

class NoneMod : AbstractMod("none", "None"), Logging {
    override fun finishModLoading() {}

    override fun createContentPane(): Pane {
        val box = VBox()

        val label = Label("No widget here :(")
        box.children.add(label)

        return box
    }

    override fun shutdownNow() {}
}