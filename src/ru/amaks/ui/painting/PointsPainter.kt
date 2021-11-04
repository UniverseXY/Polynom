package ru.amaks.ui.painting

import java.awt.*
import kotlin.math.abs

class PointsPainter(private val plane : Plane) : Painter {
    private val points: MutableMap<Double, Double> = mutableMapOf()
    val pSize = 10
    var deletedPointX : Double = 0.0 // Будем использовать это свойство, для передачи в метод удаления точки в Newton_2
    var pColor : Color = Color.GREEN
        set(value) {
            field = value
        }
    override fun paint(g: Graphics) {
        with(plane) {
            (g as Graphics2D).apply {
                stroke = BasicStroke(1F)
                color = pColor

                for (x in points.keys) {
                    drawOval(xCrt2Scr(x) - pSize / 2, yCrt2Scr(points.getValue(x)) - pSize / 2, pSize, pSize)
                    fillOval(xCrt2Scr(x) - pSize / 2, yCrt2Scr(points.getValue(x)) - pSize / 2, pSize, pSize)
                }
            }
        }
    }

    /**
     * Функция добавления точек в points, чтобы они отображались на экране
     * @return Возвращает true, если точка добавлена в points, иначе false
     */
    public fun addPoint (x : Double, y : Double) : Boolean {
        points.keys.forEach{
            if (abs(plane.xCrt2Scr(x) - plane.xCrt2Scr(it)) <= pSize) return false
        }
        points.put(x, y)
        return true
    }

    /**
     * Функция удаления точек из points, чтобы они отображались на экране
     * @return Возвращает true если точка в points была удалена, иначе возвращает false
     */
    public fun deletePoint (x : Double, y : Double) : Boolean{
        points.keys.forEach{
            if (abs(plane.xCrt2Scr(x) - plane.xCrt2Scr(it)) <= pSize / 2) {
                if (abs(plane.yCrt2Scr(y) - plane.yCrt2Scr(points.getValue(it))) <= pSize / 2) {
                    points.remove(it)
                    deletedPointX = it
                    return true
                }
            }
        }
        return false
    }
}