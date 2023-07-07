package handy_display

import handy_display.mirror.AbstractMirror
import handy_display.mirror.LcdTftMirror
import handy_display.widget.AbstractWidget
import handy_display.widget.NoneWidget
import org.apache.logging.log4j.kotlin.Logging
import picocli.CommandLine
import javax.swing.SwingUtilities

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

        val mirror: AbstractMirror = LcdTftMirror()
        val widgets: Map<String, AbstractWidget> = mapOf("null" to NoneWidget())

        if (headful) {
            logger.info("Starting Swing GUI...")
            SwingUtilities.invokeLater {
                val frame = HandyFrame(mirror)
                frame.isVisible = true

                frame.currentWidget = NoneWidget()
            }
        }
    }
}