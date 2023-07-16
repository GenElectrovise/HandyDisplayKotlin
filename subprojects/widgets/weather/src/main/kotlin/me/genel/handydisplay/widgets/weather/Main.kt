package me.genel.handydisplay.widgets.weather

import me.genel.handydisplay.widgets.api.IWidget
import java.util.function.Supplier

fun main(args: Array<String>) {

    println("Starting Handy Display!")
    println("Program arguments: ${args.joinToString()}")

}

class Main: IWidget {
    override val name: String = ""
    override val supp: () -> String = { "string" }
}