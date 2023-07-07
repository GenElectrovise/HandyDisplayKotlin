package handy_display.widget

import javax.swing.JPanel

abstract class AbstractWidget(val widgetName: String) : JPanel() {
    fun onShow() {
        isEnabled = true
        isVisible = true
    }

    fun onHide() {
        isEnabled = false
        isVisible = false
    }
}