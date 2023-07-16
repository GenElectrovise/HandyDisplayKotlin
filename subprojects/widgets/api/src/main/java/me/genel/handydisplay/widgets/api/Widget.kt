package me.genel.handydisplay.widgets.api

import java.util.function.Supplier

interface IWidget {
    val name: String
    val supp: () -> String
}
