package me.genel.handydisplay.core

import javafx.application.Platform
import javafx.stage.Stage
import org.apache.logging.log4j.kotlin.Logging
import picocli.CommandLine


/**
 * The "real" entrypoint to the application, where arguments have been parsed and tasks are delegated.
 */
class RunCommand : Runnable, Logging {

    @CommandLine.Option(
        names = ["-m", "--mirror"],
        description = ["Name of the mirror to use."],
        required = true
    )
    lateinit var mirrorName: String

    @CommandLine.Option(
        names = ["-h", "--head"],
        description = ["Whether to run the application with a mirror-independent GUI."],
        required = true
    )
    var headful: Boolean = false


    override fun run() {
        val map = mapOf("mirror" to mirrorName, "headful" to headful)
        logger.debug("Parsed arguments: $map")

        // val jsonContent = optionsFile.readText()
        // val options = Json.decodeFromString<Options>(jsonContent)

        val widgetManager = WidgetManager()
        val guiApp = GUI(widgetManager)
        guiApp.init()

        Platform.startup {
            val rootStage = Stage()
            guiApp.start(rootStage)
        }
    }
}