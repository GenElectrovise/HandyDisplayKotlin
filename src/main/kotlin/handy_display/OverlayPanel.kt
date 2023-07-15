package handy_display

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Image
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
    private val leftIcon: ImageIcon
    private val rightIcon: ImageIcon
    private val leftButton: JButton
    private val rightButton: JButton

    init {
        layout = BorderLayout()
        isOpaque = false

        barBoxLabel = JLabel(ImageIcon(barBox))
        barBoxLabel.layout = BorderLayout()
        barBoxLabel.add(JLabel("Time", JLabel.LEFT), BorderLayout.WEST)
        barBoxLabel.border = EmptyBorder(0, 5, 0, 5)
        barBoxLabel.add(JLabel("Title", JLabel.CENTER), BorderLayout.EAST)

        leftIcon = ImageIcon(leftImg)
        leftButton = JButton(leftIcon)
        leftButton.isContentAreaFilled = false
        leftButton.isBorderPainted = false
        leftButton.isFocusable = false
        leftButton.addActionListener { onLeftPressed.invoke(Unit) }

        rightIcon = ImageIcon(rightImg)
        rightButton = JButton(rightIcon)
        rightButton.isContentAreaFilled = false
        rightButton.isBorderPainted = false
        rightButton.isFocusable = false
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