package handy_display.widget

import handy_display.RunCommand
import org.apache.logging.log4j.kotlin.Logging
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.InputStream
import java.lang.Exception
import javax.imageio.ImageIO
import javax.swing.JPanel


abstract class AbstractWidget(val widgetName: String) : JPanel(), Logging {

    fun getImage(name: String): BufferedImage {
        val path = "images/$widgetName/$name"

        try {
            val stream: InputStream = AbstractWidget::class.java.classLoader.getResourceAsStream(path)
            return ImageIO.read(stream)
        } catch (e: Exception) {
            logger.fatal("Unable to read resource '$path'")
            throw e
        }
    }

    fun getFont(name: String): Font {
        val stream: InputStream = AbstractWidget::class.java.classLoader.getResourceAsStream("fonts/$name")
        return Font.createFont(Font.TRUETYPE_FONT, stream)
    }

    fun onShow() {
        isEnabled = true
        isVisible = true
    }

    fun onHide() {
        isEnabled = false
        isVisible = false
    }
}