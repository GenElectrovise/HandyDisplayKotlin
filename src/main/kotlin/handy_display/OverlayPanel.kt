package handy_display

import org.apache.logging.log4j.kotlin.Logging
import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

const val OVERLAY_GROUP = "overlay"

class OverlayPanel(
    onLeftPressed: (Unit) -> Unit,
    onRightPressed: (Unit) -> Unit
) : JPanel() {
    private val barBox = loadImage(OVERLAY_GROUP, "bar_box.bmp").getScaledInstance(480, 28, Image.SCALE_DEFAULT)
    private val leftImg = loadImage(OVERLAY_GROUP, "left_toggle.bmp").getScaledInstance(40, 74, Image.SCALE_DEFAULT)
    private val rightImg = loadImage(OVERLAY_GROUP, "right_toggle.bmp").getScaledInstance(40, 74, Image.SCALE_DEFAULT)

    private val barBoxLabel: JLabel
    //private val leftIcon: ImageIcon
    //private val rightIcon: ImageIcon
    private val leftButton: JButton
    private val rightButton: JButton

    override fun isOptimizedDrawingEnabled(): Boolean = false

    init {
        layout = BorderLayout()
        isOpaque = false

        barBoxLabel = JLabel(ImageIcon(barBox))
        barBoxLabel.layout = BorderLayout()
        barBoxLabel.add(JLabel("Time", JLabel.LEFT), BorderLayout.WEST)
        barBoxLabel.border = EmptyBorder(0, 5, 0, 5)
        barBoxLabel.add(JLabel("Title", JLabel.CENTER), BorderLayout.EAST)

        leftButton = TranslucentJButton(leftImg, 0.75f)
        leftButton.addActionListener { onLeftPressed.invoke(Unit) }

        rightButton = TranslucentJButton(rightImg, 0.75f)
        rightButton.addActionListener { onRightPressed.invoke(Unit) }

        add(barBoxLabel, BorderLayout.NORTH)
        add(leftButton, BorderLayout.WEST)
        add(rightButton, BorderLayout.EAST)

        // TODO remove debug leftButton
        leftButton.addActionListener {
            isVisible = !isVisible
        }
    }
}

class TranslucentJButton(private val image: Image, private val alpha: Float) : JButton(), Logging {

    init {
        isOpaque = false
        isContentAreaFilled = false
        isBorderPainted = false
        isFocusable = false
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        if (g == null)
            logger.warn("Null graphics context for TranslucentButton!")

        val graphics = g!!.create() as Graphics2D
        graphics.composite = AlphaComposite.SrcOver.derive(alpha)
        // graphics.background = background
        // graphics.fillRect(0, 0, width, height)
        graphics.drawImage(image, 0, 0, this)
        graphics.dispose()
    }
}



