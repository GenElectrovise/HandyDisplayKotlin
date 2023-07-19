package me.genel.handydisplay.core

import javafx.application.Platform
import javafx.stage.Stage
import org.apache.logging.log4j.kotlin.Logging
import picocli.CommandLine

lateinit var GUI: JavaFXGui
lateinit var MOD_MANAGER: ModManager

fun main(args: Array<String>) {

    println("Starting Handy Display!")
    println("Program arguments: ${args.joinToString()}")

    checkForResources()

    CommandLine(RunCommand()).execute(*args)
}

fun checkForResources() {
    val root = RunCommand::class.java.classLoader.getResource("resources_root")
        ?: throw NullPointerException("Cannot find resources_root. This indicates that the necessary JAR resources are inaccessible.")
    println("Found resources_root: $root")
    println("Resources are intact!")
}

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

        MOD_MANAGER = ModManager()
        GUI = JavaFXGui()

        GUI.init()
        Platform.startup {
            val rootStage = Stage()
            GUI.start(rootStage)
        }
    }
}