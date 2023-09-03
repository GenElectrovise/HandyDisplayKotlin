package uk.iatom.handydisplay

import javafx.application.Application
import picocli.CommandLine
import uk.iatom.handydisplay.gui.JavaFXGui
import uk.iatom.handydisplay.helpers.fileConfig
import uk.iatom.handydisplay.helpers.hdRunFile
import uk.iatom.handydisplay.services.plugin.ModulePluginLoader
import java.util.logging.*
import kotlin.system.exitProcess

/**
 * Core configuration singleton instance, loaded from core.properties.
 */
lateinit var CORE_CONFIG: CoreConfigModel

private lateinit var logger: Logger


/**
 * Welcome! This is the entry point for the HandyDisplay! Please make yourself at home...
 */
fun main(args: Array<String>) {

    println("Starting Handy Display!")
    println("Program arguments: ${args.joinToString()}")

    checkForResources()
    readConfiguration()
    configureLogging()

    logger.info("Application starting! (For real this time!)")
    CommandLine(RunCommand()).execute(*args)
}

private fun readConfiguration() {
    CORE_CONFIG = fileConfig(
            hdRunFile(
                    null,
                    "core.properties"
                     )
                            )
}

private fun configureLogging() {
    val configStream = hdRunFile(
            null,
            "handylog.properties",
            deployIfNotPresent = true
                                ).inputStream()
    LogManager
            .getLogManager()
            .readConfiguration(configStream)

    logger = Logger.getLogger("uk.iatom.handydisplay.MainKt")
    logger.severe("Logging is now configured!")
}


/**
 * Check that the JAR resources are available.
 */
fun checkForResources() {
    val root = ClassLoader
            .getSystemClassLoader()
            .getResource("resources_root")
            ?: throw NullPointerException("Cannot find resources_root. This indicates that the necessary JAR resources are inaccessible.")
    println("Resources are intact! Found resources_root: $root")
}


/**
 * The one and only PicoCLI command for HandyDisplay. Parses program arguments and runs the app.
 */
class RunCommand: Runnable {


    val logger: Logger = Logger.getLogger(javaClass.name)


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
        logger.fine("Parsed arguments: $map")

        // Reference PluginLoader singleton to initialise it
        // PluginLoader
        ModulePluginLoader

        // TODO Test RPi with GUI currently - maybe it will work??
        //https://openjfx-dev.openjdk.java.narkive.com/POqzaWTl/running-javafx-headless-without-native-dependencies
        //https://stackoverflow.com/questions/37394512/javafx-output-to-the-spi-bus-instead-of-hdmi-on-a-rpi

        try {
            // Start application (initialises GUI variable)
            Application.launch(JavaFXGui::class.java)
        } catch (e: Exception) {
            throw e
        }
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