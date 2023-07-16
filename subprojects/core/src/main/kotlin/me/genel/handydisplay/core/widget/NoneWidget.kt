package me.genel.handydisplay.core.widget

import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import org.apache.logging.log4j.kotlin.Logging

class NoneWidget : AbstractWidget("none"), Logging {

    override fun createContentPane(): Pane {
        val box = VBox()

        val label = Label("No widget here :(")
        box.children.add(label)

        return box
    }
}