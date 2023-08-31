package me.genel.handydisplay.core.gui

import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane

/**
 * Singleton provider of overlay panes.
 */
object OverlayCreator {


    fun createOverlayPane(): Pane {
        val result = FXMLHelper.loadFXML<OverlayController, StackPane>("fxml/overlay.fxml")
        return result.rootComponent
    }
}