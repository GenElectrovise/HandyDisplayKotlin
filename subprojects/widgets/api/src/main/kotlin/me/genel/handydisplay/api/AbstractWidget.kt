package me.genel.handydisplay.api

import javafx.scene.layout.Pane
import org.apache.logging.log4j.kotlin.Logging

abstract class AbstractWidget(val internalName: String, val displayName: String) : Logging {

    abstract fun createContentPane(): Pane
}