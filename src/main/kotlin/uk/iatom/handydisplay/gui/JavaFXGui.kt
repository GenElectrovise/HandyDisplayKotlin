package uk.iatom.handydisplay.gui

import javafx.application.Application
import javafx.application.ConditionalFeature
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.RowConstraints
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import uk.iatom.handydisplay.fileConfig
import uk.iatom.handydisplay.hdRunFile
import org.apache.logging.log4j.kotlin.Logging
import kotlin.system.exitProcess


const val WIDTH: Double = 480.0
const val HEIGHT: Double = 320.0


// Layers for the GUI stack pane
/**
 * The StackPane layer housing the grid which widgets are placed into
 */
const val GRID_LAYER = 0


/**
 * The StackPane layer which fancy things like the 'character' are drawn on.
 */
const val EFFECTS_LAYER = GRID_LAYER + 1


/**
 * The StackPane layer for the overlay.
 */
const val OVERLAY_LAYER = EFFECTS_LAYER + 1


/**
 * JavaFXGui singleton instance.
 */
@Volatile var GUI: JavaFXGui? = null


/**
 * The GUI for HandyDisplay! Handles the displaying of widgets and application lifecycle, plus it
 * delegates to the active mirror.
 *
 * This class is a singleton, the instance of which can be accessed by the package-level `GUI`
 * variable.
 */
class JavaFXGui: Application(), Logging {


    private val guiConfig: GuiConfigModel = try {
        fileConfig(
                hdRunFile(
                        null,
                        "gui.properties"
                         )
                  )
    } catch (e: Exception) {
        logger.fatal(e)
        e.printStackTrace()
        exitProcess(0)
    }

    private lateinit var contentStack: StackPane
    private lateinit var grid: GridPane
    private lateinit var effects: Pane
    private lateinit var overlay: Pane

    private val map: WidgetMap = WidgetMap(
            hdRunFile(
                    null,
                    "map.json"
                     )
                                          )


    init {
        if (GUI != null) throw IllegalStateException("Cannot instantiate multiple GUIs!")
        GUI = this
        checkSupportedJFXFeatures()
    }

    override fun start(primaryStage: Stage?) {
        if (primaryStage == null) throw NullPointerException("Cannot create GUI with null primaryStage!")

        // Set up stack pane (so that I can index its elements by number easily)
        contentStack = StackPane()
        for (i in 0 .. OVERLAY_LAYER) {
            contentStack.children.add(AnchorPane())
        }

        // Configure window/stage
        Platform.setImplicitExit(true)
        primaryStage.setOnCloseRequest { _ ->
            logger.fatal("Close requested - shutting down platform...")
            println("Close requested - shutting down platform...")
            Platform.exit()
        }
        primaryStage.scene = Scene(
                contentStack,
                WIDTH,
                HEIGHT
                                  )
        primaryStage.show()

        // Set up grid layer
        grid = GridPane()
        contentStack.children[GRID_LAYER] = grid
        // Set up effects layer
        effects = AnchorPane()
        contentStack.children[EFFECTS_LAYER] = effects
        // Set up overlay layer
        overlay = OverlayCreator.createOverlayPane()
        contentStack.children[OVERLAY_LAYER] = overlay

        // Add rows and columns
        for (i in 1 .. map.width) {
            val con = ColumnConstraints()
            con.prefWidth = WIDTH
            grid.columnConstraints.add(con)
        }
        for (i in 1 .. map.height) {
            val con = RowConstraints()
            con.prefHeight = WIDTH
            grid.rowConstraints.add(con)
        }

        // Fill cells with content
        map.forEachIndexed { x, y, wid ->

            // GridPane indexes from 1, but arrays from 0
            val col = x + 1;
            val row = y + 1;

            grid.add(
                    wid.createContentPane(),
                    col,
                    row
                    )
        }

        // Set up initial widget
        // TODO Set starting pos
    }


    override fun stop() {
        super.stop()
        println("Stopping application...")
    }


    /**
     * Log supported JavaFX features.
     */
    private fun checkSupportedJFXFeatures() {
        logger.debug("Supported JavaFX ConditionalFeatures:")
        ConditionalFeature.entries.forEach {
            try {
                logger.debug(" > ${it.name}: ${Platform.isSupported(it)}")
            } catch (e: Exception) {
                logger.debug(" ! FAILED ${it.name}: ${e.message}")
            }
        }
    }


    /**
     * Configuration model for the gui.properties file.
     */
    data class GuiConfigModel(
            val test: String
                             )
}