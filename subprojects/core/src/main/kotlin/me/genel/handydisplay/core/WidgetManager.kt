package me.genel.handydisplay.core

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import me.genel.handydisplay.core.widget.AbstractWidget
import me.genel.handydisplay.core.widget.NoneWidget
import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import java.io.File
import java.net.URLClassLoader

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
        val w = ArrayList<AbstractWidget>()

        w.add(NoneWidget())

        val files = File("mods/widgets").listFiles()
        val urlLoader = URLClassLoader(
            files?.map { it.toURI().toURL() }?.toTypedArray()
                ?: throw NullPointerException("Error collecting files for widget generation: $files")
        )

        val config = ConfigurationBuilder()
        config.addClassLoaders(urlLoader)
        config.setInputsFilter(
            FilterBuilder().excludePackage("org.reflections")
                // JDK Packages
                // https://docs.oracle.com/en/java/javase/11/docs/api/allpackages-index.html
                .excludePackage("com.sun")
                .excludePackage("java")
                .excludePackage("javax")
                .excludePackage("jdk")
                .excludePackage("netscape")
                .excludePackage("org.ietf")
                .excludePackage("org.w3c")
                .excludePackage("org.xml")
                // Library packages
                .excludePackage("javafx")
                .excludePackage("kotlin")
                .excludePackage("kotlinx")
                .excludePackage("sun")
        )

        val reflections = Reflections(config)
        val widgetClasses = reflections.getSubTypesOf(AbstractWidget::class.java)

        widgetClasses.forEach {
            val inst = it.getDeclaredConstructor().newInstance()
            w.add(inst)
        }

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






