package handy_display

import java.awt.*
import javax.swing.*

@Deprecated("Purely for testing and SO")
internal class TransparentPanelTest : JFrame() {


    init {
        createAndShowGUI()
    }

    private fun createAndShowGUI() {
        defaultCloseOperation = EXIT_ON_CLOSE

        contentPane = JLabel(ImageIcon("C:\\Users\\GenElectrovise\\OneDrive\\Pictures\\my photos\\Backgrounds\\bee.jpg"))
        layout = FlowLayout()

        //
        val translucentPanel: JPanel = object: JPanel() {
            override fun paintComponent(g: Graphics?) {
                super.paintComponent(g)

                val graphics = g!!.create() as Graphics2D
                graphics.composite = AlphaComposite.SrcOver.derive(0.5f)
                graphics.color = background
                graphics.fillRect(0, 0, width, height)
                graphics.dispose()
            }
        }
        translucentPanel.background = Color(0, 0, 0, 125)
        translucentPanel.preferredSize = Dimension(250, 150)
        translucentPanel.isOpaque = false

        //
        val backingPanel = JPanel()
        backingPanel.isOpaque = false
        backingPanel.layout = OverlayLayout(backingPanel)

        backingPanel.add(translucentPanel)
        backingPanel.add(Label("Test!"))

        add(backingPanel)

        setSize(600, 400)
        isVisible = true
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater { TransparentPanelTest() }
        }
    }
}