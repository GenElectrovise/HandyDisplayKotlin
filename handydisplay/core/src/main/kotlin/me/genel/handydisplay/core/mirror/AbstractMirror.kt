package me.genel.handydisplay.core.mirror

import java.awt.Dimension

abstract class AbstractMirror(val size: Dimension) {


    @Volatile var busy: Boolean = false

    abstract fun updatePixels()
}