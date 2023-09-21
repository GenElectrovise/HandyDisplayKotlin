package uk.iatom.handydisplay.services.widget

import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane

class ClockWidget : AbstractWidget("clock", "Clock") {

  override fun createContentPane(): Pane {
    return AnchorPane()
  }
}
