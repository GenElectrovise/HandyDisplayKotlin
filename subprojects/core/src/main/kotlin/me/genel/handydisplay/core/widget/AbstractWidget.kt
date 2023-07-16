package handy_display.widget

import org.apache.logging.log4j.kotlin.Logging
import javax.swing.JPanel


abstract class AbstractWidget(val widgetName: String) : Logging {


    abstract fun getContentPanel(): JPanel
}