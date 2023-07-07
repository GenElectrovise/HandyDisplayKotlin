package handy_display.widget

import org.apache.logging.log4j.kotlin.Logging
import java.awt.Color
import java.awt.Graphics
import java.awt.Label
import javax.swing.Timer

class NoneWidget : AbstractWidget("none"), Logging {

    private val label = Label("No widget here :(")
    init {
        add(label)
        background = Color.YELLOW
        isOpaque = true
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        logger.info("Painting NoneWidget!")
    }
}