package me.genel.handydisplay.core

import handy_display.loadImage
import javafx.scene.control.Label
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import java.awt.*

const val OVERLAY_GROUP = "overlay"

private val barBox = loadImage(OVERLAY_GROUP, "bar_box.bmp").getScaledInstance(480, 28, Image.SCALE_DEFAULT)
private val leftImg = loadImage(OVERLAY_GROUP, "left_toggle.bmp").getScaledInstance(40, 74, Image.SCALE_DEFAULT)
private val rightImg = loadImage(OVERLAY_GROUP, "right_toggle.bmp").getScaledInstance(40, 74, Image.SCALE_DEFAULT)

fun createOverlayPane(
    left: () -> Unit,
    right: () -> Unit
): Pane {
    val box = VBox()
    // box.style = "-fx-background: transparent; -fx-background-color: transparent; ";

    box.children.add(Label("Testing label!"))

    return box
}