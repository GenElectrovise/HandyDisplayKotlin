package handy_display

import handy_display.mirror.AbstractMirror
import javax.swing.JFrame

class MirroredJFrame(private val mirror: AbstractMirror) : JFrame() {

    override fun repaint() {
        super.repaint()

        if (!mirror.busy) {
            mirror.updatePixels()
        }
    }
}