package me.genel.handydisplay.core.widget

import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import me.genel.handydisplay.core.`AbstractMod`
import org.apache.logging.log4j.kotlin.Logging

class NoneWidget : `AbstractMod`("none", "None"), Logging {

    override fun createContentPane(): Pane {
        val box = VBox()

        val label = Label("No widget here :(")
        box.children.add(label)

        return box
    }
}