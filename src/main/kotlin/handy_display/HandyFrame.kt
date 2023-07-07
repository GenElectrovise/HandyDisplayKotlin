package handy_display

import handy_display.mirror.AbstractMirror
import handy_display.widget.AbstractWidget
import handy_display.widget.NoneWidget
import org.apache.logging.log4j.kotlin.Logging
import javax.swing.JFrame
import javax.swing.WindowConstants


class HandyFrame(private val mirror: AbstractMirror) : JFrame(), Logging {

    var currentWidget: AbstractWidget
        get() = if (contentPane is AbstractWidget) contentPane as AbstractWidget else NoneWidget()
        set(new) {
            logger.info("Swapping $currentWidget for $new")

            currentWidget.onHide()
            contentPane = new
            currentWidget.onShow()
        }

    init {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        size = mirror.size
        currentWidget = NoneWidget()
    }

    override fun repaint() {
        super.repaint()

        if (!mirror.busy) {
            mirror.updatePixels()
        }
    }
}