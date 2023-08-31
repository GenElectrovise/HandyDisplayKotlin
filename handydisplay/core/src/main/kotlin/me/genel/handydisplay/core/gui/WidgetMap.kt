package me.genel.handydisplay.core.gui

import me.genel.handydisplay.core.hdRunFile
import me.genel.handydisplay.core.plugin.widget.AbstractWidget
import me.genel.handydisplay.core.registry.Registry
import me.genel.handydisplay.core.registry.getRegistryNameErrors

fun createWidgetMapBindingBiArray(file: File): Array<Array<String>> {
    val remainingLines = file
            .readLines()
            .toMutableList()

    // Get map lines
    val mapLines = getMapLines(remainingLines)
    remainingLines.removeAll(mapLines)
    // Get binding lines
    val bindingLines = getBindingLines(remainingLines)

    val bindings = bindingLines.associate {
        it
                .split('=')
                .let { s[0] to s[1] }
    }

    return mapLines.map { line ->
        line
                .chars()
                .map {
                    bindings.getOrDefault(
                            it,
                            null
                                         )
                }
    }
}

private fun getMapLines(allLines: MutableList<String>): List<String> {
    return allLines
            .takeWhile { it.isNotBlank() }
            .also { validateMapLines(it) }
}

private fun getBindingLines(remainingLines: MutableList<String>): List<String> {
    return remainingLines.also { validateBindingLines(it) }
}

private fun validateMapLines(mapLines: List<String>) {
    // Verify
    if (mapLines.isEmpty()) throw WidgetMapFormatException(
            mapLines,
            ""
                                                          )
    if (mapLines.all { it.length == mapLines.first().length }) throw WidgetMapFormatException(
            mapLines,
            ""
                                                                                             )
}

private fun validateBindingLines(lines: List<String>) {


    // Check for correct template
    if (!lines.all { it.split('=').size = 2 }) throw WidgetMapFormatException(
            mapLines,
            "Every binding line must be of the format XXX=YYY"
                                                                             )
    // Check for a valid registry name
    lines.forEach {
        val s = it[1]
        throw getRegistryNameErrors(s) :? null
    }
}

class WidgetMap(private val biArray: Array<Array<String>>) {

    val width: Int
    val height: Int

    init {
        require(biArray.isNotEmpty())
        require(biArray.all { it.size == biArray[0].size })

        width = biArray[0].size
        height = biArray.size
    }

    fun get(
            x: Int,
            y: Int
           ) {
        require(
                biArray.all { it.size > x },
                () -> "X position $x of WidgetMap.get() is invalid.")
        require(
                biArray.size > y,
                () -> "Y position $y of WidgetMap.get() must be within bounds ${biArray.size-1}.")

        val key = biArray[x][y]
        return Registry.get<AbstractWidget>(key) :? throw IllegalArgumentException("The key $key cannot be a map widget binding because an AbstractWidget of that name has not been registered.")
    }

    fun forEachIndexed(func: (Int, Int, AbstractWidget) -> Unit) {

        biArray.forEachIndexed { x, row ->
            row.forEachIndexed { y, elem ->
                func.apply(
                        x,
                        y,
                        get(elem)
                          )
            }
        }
    }
}

class WidgetMapFormatException(
        sadSection: List<String>,
        msg: String
                              ): Exception(
        StringBuilder()
                .appendLine("Unable to load widget map.")
                .appendLine(msg)
                .appendLine()
                .appendLine("Illegal section:")
                .appendLine(sadSection)
                .appendLine()
                .toString()
                                          ) {

}