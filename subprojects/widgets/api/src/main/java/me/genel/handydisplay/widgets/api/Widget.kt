package me.genel.handydisplay.widgets.api

interface IWidget {
    val name: String
    val supp: () -> String
}
