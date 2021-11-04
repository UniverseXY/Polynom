package ru.amaks.ui

import ru.amaks.ui.painting.CartesianPainter
import ru.amaks.ui.painting.FunctionPainter
import ru.amaks.ui.painting.Painter
import java.awt.Color
import java.awt.Graphics
import java.awt.Paint
import javax.swing.JPanel

class GraphicsPanel(private val painters: List<Painter>) : JPanel() {
    override fun paint(g : Graphics?) {
        super.paint(g)
        g?.let {
            painters.forEach {
                    p -> p.paint(it)
            }
        }
    }
}