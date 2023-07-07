package handy_display.widget

import org.apache.logging.log4j.kotlin.Logging
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import java.awt.Image
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.border.EmptyBorder

class OverlayWidget : AbstractWidget("overlay"), Logging {

    private val barBox = getImage("bar_box.bmp").getScaledInstance(480, 28, Image.SCALE_DEFAULT)
    private val leftToggle = getImage("left_toggle.bmp").getScaledInstance(40, 74, Image.SCALE_DEFAULT)
    private val rightToggle = getImage("right_toggle.bmp").getScaledInstance(40, 74, Image.SCALE_DEFAULT)

    init {
        layout = BorderLayout()
        background = Color(0, 0, 0, 0)
        isOpaque = false
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        val barBoxLabel = JLabel(ImageIcon(barBox))
        barBoxLabel.layout = BorderLayout()
        barBoxLabel.add(JLabel("Time", JLabel.LEFT), BorderLayout.WEST)
        barBoxLabel.border = EmptyBorder(0, 5, 0, 5)
        barBoxLabel.add(JLabel("Title", JLabel.CENTER), BorderLayout.EAST)

        add(barBoxLabel, BorderLayout.NORTH)
        add(JLabel(ImageIcon(leftToggle)), BorderLayout.WEST)
        add(JLabel(ImageIcon(rightToggle)), BorderLayout.EAST)
    }
}