package handy_display

import java.awt.BorderLayout
import java.awt.Graphics
import javax.swing.JComponent

/**
 * A wrapper Container for holding components that use a background Color
 * containing an alpha value with some transparency.
 *
 * A Component that uses a transparent background should really have its
 * opaque property set to false so that the area it occupies is first painted
 * by its opaque ancestor (to make sure no painting artifacts exist). However,
 * if the property is set to false, then most Swing components will not paint
 * the background at all, so you lose the transparent background Color.
 *
 * This components attempts to get around this problem by doing the
 * background painting on behalf of its contained Component, using the
 * background Color of the Component.
 */
@Deprecated("Not needed, but don't want to delete yet.")
class AlphaContainer(private val component: JComponent) : JComponent() {
    init {
        layout = BorderLayout()
        isOpaque = false
        component.isOpaque = false
        add(component)
    }

    /**
     * Paint the background using the background Color of the
     * contained component
     */
    public override fun paintComponent(g: Graphics) {
        g.color = component.background
        g.fillRect(0, 0, width, height)
    }
}