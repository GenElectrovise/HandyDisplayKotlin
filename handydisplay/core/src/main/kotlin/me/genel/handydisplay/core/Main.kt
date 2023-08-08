package me.genel.handydisplay.core

import javafx.application.Application
import me.genel.handydisplay.core.gui.JavaFXGui
import me.genel.handydisplay.core.plugin.PluginLoader
import org.apache.logging.log4j.kotlin.Logging
import picocli.CommandLine
import java.awt.GraphicsEnvironment
import kotlin.system.exitProcess

/**
 * Core configuration singleton instance, loaded from core.properties.
 */
lateinit var CORE_CONFIG: CoreConfigModel


/**
 * Welcome! This is the entry point for the HandyDisplay! Please make yourself at home...
 */
fun main(args: Array<String>) {

    println("Starting Handy Display!")
    println("Program arguments: ${args.joinToString()}")

    checkForResources()

    CommandLine(RunCommand()).execute(*args)
}


/**
 * Check that the JAR resources are available.
 */
fun checkForResources() {
    val root = RunCommand::class.java.classLoader.getResource("resources_root")
            ?: throw NullPointerException("Cannot find resources_root. This indicates that the necessary JAR resources are inaccessible.")
    println("Found resources_root: $root")
    println("Resources are intact!")
}


/**
 * The one and only PicoCLI command for HandyDisplay. Parses program arguments and runs the app.
 */
class RunCommand: Runnable, Logging {


    @CommandLine.Option(
            names = ["-m", "--mirror"],
            description = ["Name of the mirror to use."],
            required = true
                       ) lateinit var mirrorName: String


    @CommandLine.Option(
            names = ["-h", "--head"],
            description = ["Whether to run the application with a mirror-independent GUI."],
            required = true
                       ) var headful: Boolean = false


    override fun run() {
        val map = mapOf(
                "mirror" to mirrorName,
                "headful" to headful
                       )
        logger.debug("Parsed arguments: $map")

        // Create configuration
        CORE_CONFIG = fileConfig(
                hdRunFile(
                        null,
                        "core.properties"
                         )
                                )

        // Reference PluginLoader singleton to initialise it
        PluginLoader

        //https://openjfx-dev.openjdk.java.narkive.com/POqzaWTl/running-javafx-headless-without-native-dependencies
        //https://stackoverflow.com/questions/37394512/javafx-output-to-the-spi-bus-instead-of-hdmi-on-a-rpi

        // Start application (initialises GUI variable)
        Application.launch(JavaFXGui::class.java)
        exitProcess(0)
    }
}


/**
 * Configuration model for the core.properties file.
 */
data class CoreConfigModel(
        val key: List<String>,
        val yay: String
                          )