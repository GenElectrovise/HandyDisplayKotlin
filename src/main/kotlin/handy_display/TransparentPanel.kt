package handy_display

import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Label
import javax.swing.*

@Deprecated("Purely for testing and SO")
internal class TransparentPanelTest : JFrame() {
    val panel: JPanel = JPanel()

    init {
        createAndShowGUI()
    }

    private fun createAndShowGUI() {
        defaultCloseOperation = EXIT_ON_CLOSE

        contentPane = JLabel(ImageIcon("C:\\Users\\GenElectrovise\\OneDrive\\Pictures\\my photos\\Backgrounds\\bee.jpg"))
        layout = FlowLayout()

        panel.background = Color(0, 0, 0, 125)
        panel.preferredSize = Dimension(250, 150)

        val bigPanel = JPanel()
        bigPanel.isOpaque = false
        bigPanel.layout = OverlayLayout(bigPanel)

        bigPanel.add(panel)
        bigPanel.add(Label("Test!"))

        add(bigPanel)

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