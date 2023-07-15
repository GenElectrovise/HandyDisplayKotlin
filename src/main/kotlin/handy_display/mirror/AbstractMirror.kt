package handy_display.mirror

import java.awt.Dimension

abstract class AbstractMirror {
    abstract val size: Dimension

    @Volatile
    var busy: Boolean = false

    abstract fun updatePixels()
}