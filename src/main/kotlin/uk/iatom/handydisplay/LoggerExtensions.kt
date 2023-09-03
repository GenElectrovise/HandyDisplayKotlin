package uk.iatom.handydisplay

import java.util.function.*
import java.util.logging.*

fun Logger.exception(
        msg: String,
        exception: Exception
                    ) = log(
        Level.SEVERE,
        msg,
        exception
                           )

fun Logger.exception(
        msg: Supplier<String>,
        exception: Exception
                    ) = exception(
        msg.get(),
        exception
                                 )