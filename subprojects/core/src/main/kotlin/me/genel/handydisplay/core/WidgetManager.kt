package me.genel.handydisplay.core

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import me.genel.handydisplay.core.widget.AbstractWidget
import me.genel.handydisplay.core.widget.NoneWidget

class WidgetManager {

    private val widgets: Map<String, AbstractWidget>
    private val order: Set<String>

    var currentWidgetName: StringProperty = SimpleStringProperty("")
        set(value) {
            if (!widgets.containsKey(value.value)) {
                throw NoSuchElementException("currentWidgetName cannot take the value $value because there is no widget associated with the name $value.")
            }
            field = value
        }
    val currentWidget: AbstractWidget
        get() {
            val name = currentWidgetName.value
            return widgets[name] ?: throw NoSuchElementException("No widget present with the name $name")
        }


    init {
        widgets = collectWidgetInstances()
        order = createOrder()
        currentWidgetName.value = order.iterator().next()
    }

    private fun collectWidgetInstances(): Map<String, AbstractWidget> {
        val w = listOf(NoneWidget())
        return w.associateBy { it.internalName }
    }

    private fun createOrder(): Set<String> {
        val o = setOf("none")  //TODO Load order from disk
        o.forEach {
            if (!widgets.containsKey(it))
                throw NoSuchElementException("There is no appropriately named widget to bind the name $it to in the given order: $o")
        }
        return o
    }
}






