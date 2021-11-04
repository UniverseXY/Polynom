package ru.amaks.ui.painting

import java.awt.*

class FunctionPainter(private val plane: Plane, var function: (Double) -> Double = Math::sin) : Painter {

    public var funColor: Color = Color.BLUE

    override fun paint(g : Graphics) {
        with (g as Graphics2D) {
                color = funColor
                stroke = BasicStroke(4F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
                val rh = mapOf(
                    RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
                    RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                    RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
                    RenderingHints.KEY_DITHERING to RenderingHints.VALUE_DITHER_ENABLE,
                    RenderingHints.KEY_STROKE_CONTROL to RenderingHints.VALUE_STROKE_PURE,
                    RenderingHints.KEY_FRACTIONALMETRICS to RenderingHints.VALUE_FRACTIONALMETRICS_ON,
                    RenderingHints.KEY_COLOR_RENDERING to RenderingHints.VALUE_COLOR_RENDER_QUALITY
                )
                setRenderingHints(rh)
                with (plane) {
                        for (x in 0 until width) {
                        // Строим отезок по пикселям
                        drawLine(x,yCrt2Scr(function(xScr2Crt(x))) , x+1, yCrt2Scr(function(xScr2Crt(x+1))))
                     }

                }
        }
    }
}