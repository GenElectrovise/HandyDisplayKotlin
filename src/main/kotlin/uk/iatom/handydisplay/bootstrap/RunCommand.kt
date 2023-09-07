package uk.iatom.handydisplay.bootstrap

import com.almasb.fxgl.app.GameApplication
import picocli.CommandLine
import uk.iatom.handydisplay.fxgl.HDApp
import uk.iatom.handydisplay.services.plugin.ModulePluginLoader
import java.util.logging.*
import kotlin.system.exitProcess

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
            GameApplication.launch(
                    HDApp::class.java,
                    arrayOf()
                                  )
        } catch (e: Exception) {
            throw e
        }
        exitProcess(0)
    }
}