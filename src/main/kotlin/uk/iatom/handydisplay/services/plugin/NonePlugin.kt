package uk.iatom.handydisplay.services.plugin

import com.almasb.fxgl.input.Input
import java.util.logging.*
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import uk.iatom.handydisplay.fxgl.HDApp
import uk.iatom.handydisplay.services.widget.AbstractWidget

class NonePlugin : AbstractPlugin("none") {

  val logger = Logger.getLogger(javaClass.name)

  override fun finishPluginLoading() {}

  override fun onInitInput(input: Input) {
    logger.info("Initialising NonePlugin inputs...")
  }

  override fun onInitGame(hdApp: HDApp) {
    logger.info("Initialising NonePlugin game objects...")
  }

  override fun shutdownNow() {}

  override val registryName: String = "none"
}

class NoneWidget : AbstractWidget("none", "None") {

  override fun createContentPane(): Pane {
    val box = VBox()

    val label = Label("No widget here :(")
    box.children.add(label)

    return box
  }
}
