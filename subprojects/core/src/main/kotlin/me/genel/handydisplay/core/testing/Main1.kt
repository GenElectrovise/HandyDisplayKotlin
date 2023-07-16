package me.genel.handydisplay.core.testing

import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

class Main {
    init {
        EventQueue.invokeLater {
            val frame = JFrame()
            frame.add(TestPane())
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }

    inner class TestPane : JPanel() {
        init {
            layout = BorderLayout()
            background = Color.RED
            border = EmptyBorder(32, 32, 32, 32)

            val overlayPane = JPanel()
            overlayPane.isOpaque = false
            overlayPane.layout = OverlayLayout(overlayPane)

            val translucentPane = TranslucentPane()
            translucentPane.alpha = 0.5f
            translucentPane.background = Color.BLACK
            translucentPane.add(JLabel("Another test!"))

            overlayPane.add(translucentPane)
            overlayPane.add(JLabel("This is a test"))

            add(overlayPane)

            val slider = JSlider(0, 100)
            slider.addChangeListener {
                val alpha = slider.value / 100f
                translucentPane.alpha = alpha
            }
            add(slider, BorderLayout.SOUTH)
        }
    }

    inner class TranslucentPane : JPanel() {
        var alpha = 1f
            set(alpha) {
                field = alpha
                repaint()
            }

        init {
            isOpaque = false
        }

        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            val g2d = g.create() as Graphics2D
            g2d.composite = AlphaComposite.SrcOver.derive(alpha)
            g2d.color = background
            g2d.fillRect(0, 0, width, height)
            g2d.dispose()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main()
        }
    }
}