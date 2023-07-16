package me.genel.handydisplay.core.widget

import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import me.genel.handydisplay.core.widget.AbstractWidget
import org.apache.logging.log4j.kotlin.Logging

class NoneWidget : AbstractWidget("none"), Logging {

    override fun createContentPane(): Pane {
        val box = VBox()

        val label = Label("No widget here :(")
        box.children.add(label)

        return box
    }
}