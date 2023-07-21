package me.genel.handydisplay.api

import javafx.scene.layout.Pane
import org.apache.logging.log4j.kotlin.Logging
import java.io.File

fun hdRunFile(widget: AbstractWidget?, path: String) = File("hdrun/${widget?.internalName ?: ""}", path)

abstract class AbstractWidget(val internalName: String, val displayName: String) : Logging {

    init {
        // Internal
        assert(
            internalName.asSequence()
                .all { it.isLetterOrDigit() }) { "Internal widget names may only contain letters and digits. $internalName is invalid." }
        assert(internalName.length in 1..32) { "Internal widget names must have (0,32] characters. $internalName is invalid." }
        // Display
        assert(
            displayName.asSequence()
                .all { it.isLetterOrDigit() }) { "Displayed widget names may only contain letters and digits. $displayName is invalid." }
        assert(displayName.length in 1..16) { "Displayed widget names must have (0,16] characters. $displayName is invalid." }

    }

    abstract fun createContentPane(): Pane

    fun widgetFile(path: String) = hdRunFile(this, path)
}