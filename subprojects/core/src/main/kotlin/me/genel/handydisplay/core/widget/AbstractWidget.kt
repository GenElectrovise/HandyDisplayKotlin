package me.genel.handydisplay.core.widget

import javafx.scene.Scene
import javafx.scene.layout.Pane
import org.apache.logging.log4j.kotlin.Logging


abstract class AbstractWidget(val widgetName: String) : Logging {

    abstract fun createContentPane(): Pane
}