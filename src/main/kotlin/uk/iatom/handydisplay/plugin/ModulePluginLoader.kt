package uk.iatom.handydisplay.plugin

import org.apache.logging.log4j.kotlin.Logging
import uk.iatom.handydisplay.runDir
import java.lang.module.ModuleFinder
import kotlin.io.path.Path

object ModulePluginLoader: Logging {

    init {
        logger.info("Loading plugin modules...")

        val path = Path(runDir.absolutePath)
        val finder = ModuleFinder.of(path)
        val moduleNames = finder
                .findAll()
                .map {
                    it
                            .descriptor()
                            .name()
                }

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

        logger.info("Loaded ${newLayer.modules().size} plugin modules!")
        newLayer
                .modules()
                .forEach { logger.debug(" > ${it.name}") }
    }
}