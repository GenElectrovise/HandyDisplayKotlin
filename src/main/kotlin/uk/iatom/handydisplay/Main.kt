package uk.iatom.handydisplay

import java.util.logging.*
import picocli.CommandLine
import uk.iatom.handydisplay.bootstrap.CoreConfigModel
import uk.iatom.handydisplay.bootstrap.RunCommand
import uk.iatom.handydisplay.helpers.hdRunFile
import uk.iatom.handydisplay.helpers.propertiesConfig

/** Core configuration singleton instance, loaded from core.properties. */
var CORE_CONFIG: CoreConfigModel? = null

private lateinit var logger: Logger

/** Welcome! This is the entry point for the HandyDisplay! Please make yourself at home... */
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
  CORE_CONFIG = propertiesConfig(hdRunFile(null, "core.properties"))
}

private fun configureLogging() {
  val logsDirectory = hdRunFile(null, "logs/", deployIfNotPresent = false)
  if (!logsDirectory.exists()) {
    logsDirectory.mkdirs()
  }

  val configStream = hdRunFile(null, "handylog.properties", deployIfNotPresent = true).inputStream()
  LogManager.getLogManager().readConfiguration(configStream)

  logger = Logger.getLogger("uk.iatom.handydisplay.MainKt")
  logger.severe("Logging is now configured!")
}

/** Check that the JAR resources are available. */
fun checkForResources() {
  val root =
      ClassLoader.getSystemClassLoader().getResource("resources_root")
          ?: throw NullPointerException(
              "Cannot find resources_root. This indicates that the necessary JAR resources are inaccessible.")
  println("Resources are intact! Found resources_root: $root")
}
