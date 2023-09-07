package uk.iatom.handydisplay.fxgl

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.getInput
import uk.iatom.handydisplay.registry.Registry
import uk.iatom.handydisplay.services.plugin.AbstractPlugin
import java.util.logging.*

/**
 * https://github.com/AlmasB/FXGL-MavenGradle/blob/master/src/main/java/com/almasb/fxglgames/drop/DropApp.java
 */
object HDApp: GameApplication() {


    val logger = Logger.getLogger(javaClass.name)

    override fun initSettings(settings: GameSettings?) {
        logger.info("Initialising settings...")
        require(settings != null) { "GameSettings cannot be null at initialisation!" }
        settings.title = "HandyDisplay"
        settings.credits = listOf("Adam Spencer")
        settings.version = "dev"
    }

    override fun initInput() {
        logger.info("Initialising plugin inputs...")
        val input = getInput()
        Registry
                .getAll<AbstractPlugin>()
                .forEach { it.onInitInput(input) }

        logger.info("Initialising core inputs...")
    }

    override fun initGame() {
        logger.info("Initialising plugin game objects...")
        Registry
                .getAll<AbstractPlugin>()
                .forEach { it.onInitGame(this) }

        logger.info("Initialising core game objects...")
    }


    fun launchPublic(args: Array<String>) {
        launch(args)
    }
}