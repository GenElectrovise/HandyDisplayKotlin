package uk.iatom.handydisplay

import java.util.function.*
import java.util.logging.*

fun Logger.exception(exception: Exception, msg: Supplier<String>) =
    log(Level.SEVERE, msg.get(), exception)
