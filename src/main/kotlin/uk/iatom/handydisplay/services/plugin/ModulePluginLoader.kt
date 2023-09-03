package uk.iatom.handydisplay.services.plugin

import org.apache.logging.log4j.kotlin.Logging
import uk.iatom.handydisplay.helpers.hdRunFile
import java.lang.module.ModuleFinder
import kotlin.io.path.Path

object ModulePluginLoader: Logging {

    init {
        logger.info("Loading plugin modules...")

        val path = Path(
                hdRunFile(
                        null,
                        "plugins/",
                        false
                         ).absolutePath
                       )
        logger.debug("Searching path: $path")

        val finder = ModuleFinder.of(path)
        val moduleNames = finder
                .findAll()
                .map {
                    it
                            .descriptor()
                            .name()
                }
        logger.debug("Found ${moduleNames.size} potential modules: ${moduleNames.joinToString(", ")}")

        val bootLayer = ModuleLayer.boot()
        val configuration = bootLayer
                .configuration()
                .resolve(
                        finder,
                        ModuleFinder.of(),
                        moduleNames
                        )

        val systemLoader = ClassLoader.getSystemClassLoader()
        val newLayer = bootLayer.defineModulesWithOneLoader(
                configuration,
                systemLoader
                                                           )

        logger.info("Loaded ${newLayer.modules().size} plugin modules: ${
            newLayer
                    .modules()
                    .joinToString(separator = ", ") { it.name }
        }")
    }
}