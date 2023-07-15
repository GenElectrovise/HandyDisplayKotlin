package handy_display.widget

import org.apache.logging.log4j.kotlin.Logging
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Label
import javax.swing.JPanel

class NoneWidget : AbstractWidget("none"), Logging {

    override fun getContentPanel(): JPanel {
        val panel = JPanel()
        panel.isOpaque = true
        panel.background = Color.YELLOW

        val layout = BorderLayout()
        panel.layout = layout

        val label = Label("No widget here :(")

        panel.add(label, BorderLayout.CENTER)

        return panel
    }
}