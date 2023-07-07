package handy_display

import handy_display.mirror.AbstractMirror
import handy_display.widget.AbstractWidget
import org.apache.logging.log4j.kotlin.Logging
import javax.swing.JFrame
import javax.swing.WindowConstants


class HandyFrame(private val mirror: AbstractMirror) : JFrame(), Logging {

    var currentWidget: AbstractWidget
        get() = contentPane as AbstractWidget
        set(new) {
            contentPane = new
        }

    init {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        size = mirror.size
    }

    fun showWidget(next: AbstractWidget) {
        logger.info("Swapping $currentWidget for $next")

        currentWidget.onHide()
        currentWidget = next
        currentWidget.onShow()
    }

    override fun repaint() {
        super.repaint()

        if (!mirror.busy) {
            mirror.updatePixels()
        }
    }
}