package handy_display

import handy_display.mirror.AbstractMirror
import handy_display.widget.AbstractWidget
import handy_display.widget.NoneWidget
import handy_display.widget.OverlayWidget
import org.apache.logging.log4j.kotlin.Logging
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle
import javax.swing.JFrame
import javax.swing.JLayeredPane
import javax.swing.JPanel
import javax.swing.OverlayLayout
import javax.swing.WindowConstants


class HandyFrame(private val mirror: AbstractMirror) : JFrame(), Logging {

    private val rootPanel = object: JPanel() {
        override fun isOptimizedDrawingEnabled(): Boolean = false
    }

    private val _overlayWidget = OverlayWidget()
    private var _currentWidget: AbstractWidget = NoneWidget()

    init {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        val layout = OverlayLayout(rootPanel)
        rootPanel.layout = layout

        isResizable = false
        preferredSize = mirror.size

        add(rootPanel)

        showWidget(NoneWidget())
    }

    fun showWidget(new: AbstractWidget) {
        logger.info("Swapping $_currentWidget for $new")

        _currentWidget.onHide()
        rootPanel.removeAll()

        rootPanel.add(_overlayWidget) //TODO Try swapping these around (trust me it's weird)
        rootPanel.add(new)
        _currentWidget.onShow()

        pack()
    }

    override fun repaint() {
        super.repaint()

        if (!mirror.busy) {
            mirror.updatePixels()
        }
    }
}