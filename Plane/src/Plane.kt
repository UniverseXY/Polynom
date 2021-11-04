package ru.amaks.ui.painting
import java.awt.Dimension
import java.lang.Math.max
class Plane(
   xMin : Double,
   xMax: Double,
   yMin : Double,
   yMax : Double
) {
    var pixelSize: Dimension = Dimension(1, 1)
        set(size) {
            field = Dimension(max(1,size.width), max(1, size.height))
        }
    private var xSize: Int
        get() = pixelSize.width
        set(w) {pixelSize.width = w}
    private var ySize: Int
        get() = pixelSize.height
        set(h) {pixelSize.height = h}
    var xMin : Double = 0.0
        private set
    var xMax: Double = 0.0
        private set
    var yMin : Double = 0.0
        private set
    var yMax : Double = 0.0
        private set
    var width: Int
        get () = xSize - 1
        set(value) {
            xSize= max(1, value)
        }
    var height: Int
        get() = ySize - 1
        set(value) {
            ySize = max(1, value)
        }
    var xSegment: Pair<Double, Double>
        get() = Pair(xMin, xMax)
        set(value) {
            val k = if (value.first == value.second) 0.1 else 0.0
            if (value.first <= value.second){
                this.xMin = value.first - k
                this.xMax = value.second + k
            } else {
                this.xMax = value.first
                this.xMin = value.second
            }
        }
    var ySegment: Pair<Double, Double>
        get() = Pair(yMin, yMax)
        set(value) {
            val k = if (value.first == value.second) 0.1 else 0.0
            if (value.first <= value.second){
                this.yMin = value.first - k
                this.yMax = value.second + k
            } else {
                this.yMax = value.first
                this.yMin = value.second
            }
        }
    // Кол-во пикселей на единицу масштаба по оси X
    val xDen : Double
        get() = width / (xMax - xMin)
    // Кол-во пикселей на единицу масштаба по оси Y
    val yDen : Double
        get() = height / (yMax - yMin)

    // Задаем границы осей
    init {
        xSegment = Pair(xMin, xMax)
        ySegment = Pair(yMin, yMax)
    }

    /**
     * Перевод из Декартовых координат оси Х в экранные
     */
     fun xCrt2Scr(x : Double): Int {
         var r = ((x - xMin) * xDen).toInt()
         if (r < -width) r = - width
         if (r > 2 * width) r = 2 * width
         return r
     }
    /**
     * Перевод из Декартовых координат оси Y в экранные
     */
    fun yCrt2Scr(y : Double): Int {
        var r = ((yMax - y) * yDen).toInt()
        if (r < -height) r = - height
        if (r > 2 * height) r = 2 * height
        return r
     }
    /**
     * Перевод из экранных координат в Декартовы на оси Х
     */
    fun xScr2Crt(x : Int): Double {
        var _x = x
        if (_x < -width) _x = -width
        if (_x > 2 * width) _x = 2 * width
        return _x / xDen + xMin
    }
    /**
     * Перевод из экранных координат в Декартовы на оси Y
     */
    fun yScr2Crt (y : Int) : Double {
        var _y = y
        if (_y < -height) _y = -height
        if (_y > 2 * height) _y = 2 * height
        return yMax - y / yDen
    }

}
