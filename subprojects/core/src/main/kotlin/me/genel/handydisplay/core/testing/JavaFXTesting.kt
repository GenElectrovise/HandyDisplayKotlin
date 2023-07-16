package me.genel.handydisplay.core.testing

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.io.FileInputStream


var APP: JavaFXTesting? = null

class JavaFXTesting: Application() {

    init {
        println("Init app")
        APP = this
    }

    override fun start(primaryStage: Stage) {
        primaryStage.title = "OK"


        val bee = Image(FileInputStream("C:\\Users\\GenElectrovise\\OneDrive\\Pictures\\my photos\\Backgrounds\\bee.jpg"))
        val colours = Image(FileInputStream("C:\\Users\\GenElectrovise\\OneDrive\\Pictures\\my photos\\Backgrounds\\alpha_testing.png"))

        val stackPane = StackPane(ImageView(bee), ImageView(colours))
        val scene = Scene(stackPane, 200.0, 250.0)

        scene.fill = Color.TRANSPARENT
        // primaryStage.initStyle(StageStyle.TRANSPARENT)

        primaryStage.scene = scene
        primaryStage.show()
    }

    fun test() {
        println("Test!")
    }

}

fun main() {
    Thread { Application.launch(JavaFXTesting::class.java) }.start()
    println("launched")
    while (APP == null) { Thread.sleep(100) }
    APP?.test()
}