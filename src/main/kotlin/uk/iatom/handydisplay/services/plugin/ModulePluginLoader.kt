package uk.iatom.handydisplay.services.plugin

import java.lang.module.ModuleFinder
import java.util.logging.*
import kotlin.io.path.Path
import uk.iatom.handydisplay.helpers.hdRunFile

object ModulePluginLoader {

  val logger = Logger.getLogger(javaClass.name)

  init {
    logger.info("Loading plugin modules...")

    val path = Path(hdRunFile(null, "plugins/", false).absolutePath)
    logger.fine("Searching path: $path")

    val finder = ModuleFinder.of(path)
    val moduleNames = finder.findAll().map { it.descriptor().name() }
    logger.info("Found ${moduleNames.size} potential modules: ${moduleNames.joinToString(", ")}")

    val bootLayer = ModuleLayer.boot()
    val configuration = bootLayer.configuration().resolve(finder, ModuleFinder.of(), moduleNames)

    val systemLoader = ClassLoader.getSystemClassLoader()
    val newLayer = bootLayer.defineModulesWithOneLoader(configuration, systemLoader)

    logger.info(
        "Loaded ${newLayer.modules().size} plugin modules: ${
            newLayer
                    .modules()
                    .joinToString(separator = ", ") { it.name }
        }")
  }
}
