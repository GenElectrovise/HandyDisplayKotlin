package me.genel.handydisplay.core.mirror

import org.apache.logging.log4j.kotlin.Logging
import java.awt.Dimension

class LcdTftMirror(override val size: Dimension = Dimension(480, 320)) : AbstractMirror(), Logging {

    override fun updatePixels() {
        logger.debug("Updating pixels!")
    }

}