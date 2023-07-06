import handy_display.RunCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine

fun main(args: Array<String>) {

    val logger = LoggerFactory.getLogger("root")
    logger.info("Starting Handy Display!")
    logger.info("Program arguments: ${args.joinToString()}")

    checkForResources(logger)

    CommandLine(RunCommand()).execute(*args)
}

fun checkForResources(logger: Logger) {
    if (RunCommand::class.java.classLoader.getResource("resources_root") == null) {
        throw NullPointerException("Cannot find resources_root. This indicates that the necessary JAR resources are inaccessible.")
    }

    logger.debug("Resources are intact!")
}