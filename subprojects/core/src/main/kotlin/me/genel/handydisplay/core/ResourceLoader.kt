package handy_display

import me.genel.handydisplay.core.widget.AbstractWidget
import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.logger
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

private val logger: KotlinLogger = logger("ResourceLoader")

fun loadImage(group: String, name: String): BufferedImage {
    val path = "images/$group/$name"

    try {
        val stream: InputStream = AbstractWidget::class.java.classLoader.getResourceAsStream(path)
        return ImageIO.read(stream)
    } catch (e: Exception) {
        logger.fatal("Unable to read resource '$path'")
        throw e
    }
}

fun loadFont(name: String): Font {
    val stream: InputStream = AbstractWidget::class.java.classLoader.getResourceAsStream("fonts/$name")
    return Font.createFont(Font.TRUETYPE_FONT, stream)
}