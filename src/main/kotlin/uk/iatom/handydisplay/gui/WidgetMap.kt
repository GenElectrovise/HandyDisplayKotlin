package uk.iatom.handydisplay.gui

import uk.iatom.handydisplay.helpers.fileConfig
import uk.iatom.handydisplay.services.plugin.NoneWidget
import uk.iatom.handydisplay.services.widget.AbstractWidget
import java.io.File
import java.util.*

fun loadWidgetMapFromJsonFile(configFile: File): List<List<AbstractWidget>> {
    val config = fileConfig<WidgetMapConfig>(configFile)

    require(config.map.isNotEmpty())
    require(config.map.all { it.length == config.map[0].length })
    require(config.bindings.isNotEmpty())

    // Create bindings
    val bindings: MutableMap<String, AbstractWidget> = mutableMapOf()
    val widgetServiceLoader = ServiceLoader.load(AbstractWidget::class.java)
    config.bindings.mapValuesTo(bindings) { entry ->
        widgetServiceLoader
                .stream()
                .filter { entry.value == it.get().registryName }
                .findFirst()
                .get()
                .get()
                ?: throw IllegalStateException("The map.json expected the widget ${entry.value} which does not exist.")
    }

    // Map to widgets
    return config.map.map { line ->
        line
                .split("")
                .map { letter ->
                    bindings.getOrDefault(
                            letter,
                            NoneWidget()
                                         )
                }
    }
}

data class WidgetMapConfig(
        val map: List<String>,
        val bindings: Map<String, String>
                          )

class WidgetMap(private val biArray: List<List<AbstractWidget>>) {

    constructor(configFile: File): this(loadWidgetMapFromJsonFile(configFile))

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
           ): AbstractWidget {
        require(biArray.all { it.size > x }) { "X position $x of WidgetMap.get() is invalid." }
        require(
                biArray.size > y
               ) { "Y position $y of WidgetMap.get() must be within bounds ${biArray.size - 1}." }

        return biArray[x][y]
    }

    fun forEachIndexed(func: (Int, Int, AbstractWidget) -> Unit) {

        biArray.forEachIndexed { x, row ->
            row.forEachIndexed { y, elem ->
                func.invoke(
                        x,
                        y,
                        elem
                           )
            }
        }
    }
}