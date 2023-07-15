package handy_display

import handy_display.widget.AbstractWidget
import org.apache.logging.log4j.kotlin.Logging
import java.awt.CardLayout
import java.awt.Color
import javax.swing.JPanel
import javax.swing.OverlayLayout


class GuiPanel(widgets: List<AbstractWidget>) : JPanel(), Logging {

    private val overlayPanel: OverlayPanel
    private val cardsPanel: JPanel

    override fun isOptimizedDrawingEnabled(): Boolean = false

    init {
        overlayPanel = OverlayPanel(
            { cycleWidgetCards(false) },
            { cycleWidgetCards(true) }
        )
        cardsPanel = createWidgetCards(widgets)

        layout = OverlayLayout(this)

        overlayPanel.alignmentX = 0f
        overlayPanel.alignmentY = 0f
        cardsPanel.alignmentX = 0.5f
        cardsPanel.alignmentY = 0f

        add(overlayPanel)
        add(cardsPanel)
    }

    private fun createWidgetCards(widgets: List<AbstractWidget>): JPanel {
        val cards = JPanel()
        val layout = CardLayout()

        cards.layout = layout

        widgets.forEach { cards.add(it.widgetName, it.getContentPanel()) }
        return cards
    }

    private fun cycleWidgetCards(forwards: Boolean) {

        val layout = cardsPanel.layout as CardLayout
        logger.info("Cycling widgets " + (if (forwards) "forwards" else "backwards"))

        if (forwards)
            layout.next(cardsPanel)
        else
            layout.previous(cardsPanel)
    }
}


