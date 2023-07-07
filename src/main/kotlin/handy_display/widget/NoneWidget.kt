package handy_display.widget

import org.apache.logging.log4j.kotlin.Logging
import java.awt.Label

class NoneWidget : AbstractWidget("none"), Logging {

    init {
        add(Label("Cool beans!"))
    }

}