package uk.iatom.handydisplay

import picocli.CommandLine
import uk.iatom.handydisplay.bootstrap.CoreConfigModel
import uk.iatom.handydisplay.bootstrap.RunCommand
import uk.iatom.handydisplay.helpers.fileConfig
import uk.iatom.handydisplay.helpers.hdRunFile
import java.util.logging.*

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