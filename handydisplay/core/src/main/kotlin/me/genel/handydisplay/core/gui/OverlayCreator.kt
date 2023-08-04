package me.genel.handydisplay.core.gui

import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane

object OverlayCreator {


    fun createOverlayPane(
            left: () -> Unit,
            right: () -> Unit
                         ): Pane {

        val result = FXMLHelper.loadFXML<OverlayController, StackPane>("fxml/overlay.fxml")
        result.controller.leftToggleAction = left
        result.controller.rightToggleAction = right
        return result.rootComponent

//    val url = JavaFXGui::class.java.classLoader.getResource("fxml/overlay.fxml")
//    val loader = FXMLLoader(url)
//    return loader.load<StackPane>()
    }
}