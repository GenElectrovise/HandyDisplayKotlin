package uk.iatom.handydisplay.fxgl

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.getInput
import uk.iatom.handydisplay.registry.Registry
import uk.iatom.handydisplay.services.plugin.AbstractPlugin
import java.util.*
import java.util.logging.*

/**
 * [...](https://github.com/AlmasB/FXGL-MavenGradle/blob/master/src/main/java/com/almasb/fxglgames/drop/DropApp.java)
 */
class HDApp: GameApplication() {


    companion object {


        lateinit var APP: HDApp
    }


    private val logger = Logger.getLogger(javaClass.getName())

    init {
        logger.info("Creating new HDApp...")
        APP = this
    }


    public override fun initSettings(settings: GameSettings) {
        logger.info("Initialising settings...")
        Objects.requireNonNull(
                settings,
                "GameSettings cannot be null at initialisation!"
                              )
        settings.title = "HandyDisplay"
        settings.credits = listOf("Adam Spencer")
        settings.version = "dev"
    }

    public override fun initInput() {
        val input = getInput()
        logger.info("Initialising plugin inputs...")
        Registry
                .getAll<AbstractPlugin>()
                ?.forEach { it.onInitInput(input) }
        logger.info("Initialising core inputs...")
    }

    public override fun initGame() {
        logger.info("Initialising plugin game objects...")
        Registry
                .getAll<AbstractPlugin>()
                ?.forEach { it.onInitGame(this) }
        logger.info("Initialising core game objects...")
    }
}