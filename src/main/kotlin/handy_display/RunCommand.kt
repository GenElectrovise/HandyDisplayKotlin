package handy_display

import org.apache.logging.log4j.kotlin.Logging
import picocli.CommandLine

/**
 * The "real" entrypoint to the application, where arguments have been parsed and tasks are delegated.
 */
class RunCommand : Runnable, Logging {

    @CommandLine.Option(names = ["-m", "--mirror"], description = ["Path to the mirror JAR file."], required = true)
    var mirrorPath: String = "<no-path>"

    @CommandLine.Option(
        names = ["-h", "--head"],
        description = ["Whether to run the application with a mirror-independent GUI."],
        required = true
    )
    var head: Boolean = false


    override fun run() {
        val map = mapOf("mirror" to mirrorPath, "head" to head)
        logger.debug("Parsed arguments: $map")


    }
}