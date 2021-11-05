package ru.amaks.ui.painting

import ru.amaks.math.eq
import kotlin.math.abs
import ru.amaks.math.le
import ru.amaks.math.neq
import java.awt.*
import kotlin.math.roundToInt

class CartesianPainter(private val plane : Plane ) : Painter {
    val axesColor : Color = Color.GRAY
    private var LargeTickClr = Color.RED
    private var stepX : Double = 0.1
    private var stepY : Double = 0.1
    private var MidTickClr = Color.BLUE
    private var SmallTickClr = Color.BLACK
    private val LargeTickSz = 12
    private val MidTickSz = LargeTickSz / 2
    private val SmallTickSz = MidTickSz / 2
    private val wholePointsX = mutableListOf<Double>()
    private val wholePointsY = mutableListOf<Double>()
    var mainFont: Font = Font("Cambria", Font.BOLD, 14)
    override fun paint(g: Graphics) {
        g.apply {
            paintAxes(this)
        }
    }

    private fun paintAxes(g: Graphics) {
        with(plane) {
            (g as Graphics2D).apply {
                stroke = BasicStroke(3F)
                color = axesColor
                if (yMin <= 0 && yMax >= 0) {
                    drawLine(0, yCrt2Scr(0.0), width, yCrt2Scr(0.0))
                    paintTicksX(this, false)
                   //paintTicksY(this)
                } else {
                    // Оставить только разметку без рисования 2-х осей
                    //paintTicksY(this)
                    paintTicksX(this, true)
                    //drawLine(0, height, width, height)
                }
                color = axesColor
                stroke = BasicStroke(3F)
                if (xMin <= 0 && xMax >= 0) {
                    drawLine(xCrt2Scr(0.0), 0, xCrt2Scr(0.0), height)
                    paintTicksY(this, false)
                } else {
                    /*drawLine(0, 0, 0, height)
                    drawLine(width, 0, width, height)*/
                    paintTicksY(this, true)
                }
            }
        }
    }

   /* private fun paintTicksX(g: Graphics) {
        with(plane) {
            var currPx = 0.0
            val currX = xScr2Crt(0)
            val zeroPoint = xCrt2Scr(0.0)
            val epsilon = xCrt2Scr(0.1) - zeroPoint
            while (currPx <= width) {
            // Заменить на neq из Double
                (g as Graphics2D).apply {
                    stroke = BasicStroke(1.5F)
                  /*  if (abs(currPx - zeroPoint) < epsilon / 5)
                        color = axesColor*/
                    color = bigTickClr
                    if (!(abs(currPx - zeroPoint) <= epsilon / 5)) {
                        drawLine(currPx.toInt(), yCrt2Scr(0.3), currPx.toInt(), yCrt2Scr(-0.3))
                    }
                    var k = 1
                    color = Color.BLACK
                    while (k < 10) {
                        drawLine(
                            (currPx + k * (xDen / 10)).toInt(),
                            yCrt2Scr(0.2),
                            (currPx + k * (xDen / 10)).toInt(),
                            yCrt2Scr(-0.2)
                        )
                        k++
                      }
                }
                currPx += xDen
            }
        }
    } */
    /**
     * Рисование разметки по оси X
     */
    private fun paintTicksX(g: Graphics, isLimit : Boolean) {
        with(plane) {
            val yZero = if (!isLimit) yCrt2Scr(0.0) else  if (yMin > 0)  height else 0 // Узнаем где проходит ось Y (нужно для рисования тиков)
            val firstWholeX = xMin.toInt().toDouble() // Самая левая целая точка, которая отображается на видимой части оси X
            val lastWholeX = xMax.toInt().toDouble() // Самая правая целая точка, которая отображается на видимой части оси X
            (g as Graphics2D).apply {
                stroke = BasicStroke(1F)
                var currX = xMin // Присваиваем текущее значение абсциссы, начиная включительно с этого значения будем отрисовывать все остальные
                // Ведем цикл отрисовки до первого целого числа (не включительно)
                // В этом цикле будем отрисовывать только дробные части, включая половинки
                while (currX < firstWholeX)
                {
                    if (abs(currX * 10).toInt() % 10 == 5 ) // Проверка на половинку
                    {
                        color = MidTickClr
                        drawLine(xCrt2Scr(currX), yZero -  MidTickSz, xCrt2Scr(currX), yZero + MidTickSz)
                    }
                    else
                    {
                        color = SmallTickClr
                        drawLine(xCrt2Scr(currX), yZero - SmallTickSz, xCrt2Scr(currX), yZero + SmallTickSz)
                    }
                    currX += stepX
                    currX = (currX * 10).roundToInt() / 10.0 // Здесь оставляю только одну цифру после запятой
                }
                currX = firstWholeX // Теперь текущая абсцисса - первое целое число
                wholePointsX.clear()
                // В этом цикле отрисовываются одновременно большие, средние и маленькие тики
                while (currX <= lastWholeX) {
                    color = LargeTickClr
                    if ((currX * 10).toInt() != 0) {
                        drawLine(
                            xCrt2Scr(currX),
                            yZero - LargeTickSz,
                            xCrt2Scr(currX),
                            yZero + LargeTickSz
                        )
                        wholePointsX.add(currX)
                    }
                    var k = 1
                    // Ведем цикл отрисовки маленьких и средних тиков
                    while (k <= 10)
                    {
                        if (k != 5)
                        {
                            color = SmallTickClr
                            drawLine(xCrt2Scr(currX + k * stepX), yZero- SmallTickSz, xCrt2Scr(currX + k * stepX), yZero + SmallTickSz)
                        }
                        else
                        {
                            color = MidTickClr
                            drawLine(xCrt2Scr(currX + k * stepX), yZero- MidTickSz, xCrt2Scr(currX + k * stepX), yZero + MidTickSz)
                        }
                        k++
                    }
                    currX += stepX * 10
                }
                drawXLabels(this,yZero)
            }
        }
    }

    /**
     * Рисование разметки по оси Y
     */
    private fun paintTicksY(g: Graphics, isLimit: Boolean) {
        with(plane) {
            val xZero = if (!isLimit) xCrt2Scr(0.0) else  if (xMin > 0)  0 else width
            val firstWholeY = yMin.toInt().toDouble() // Самая нижняя целая точка, которая отображается на видимой части оси Y
            val lastWholeY = yMax.toInt().toDouble() // Самая верхняя целая точка, которая отображается на видимой части оси Y
            (g as Graphics2D).apply {
                stroke = BasicStroke(1F)
                var currY = yMin // Присваиваем текущее значение ординаты, начиная включительно с этого значения будем отрисовывать все остальные
                // Ведем цикл отрисовки до первого целого числа (не включительно)
                // В этом цикле будем отрисовывать только дробные части, включая половинки
                while (currY < firstWholeY)
                {
                    if (abs(currY * 10).toInt() % 10 == 5 ) // Проверка на половинку
                    {
                        color = MidTickClr
                        drawLine(xZero - MidTickSz, yCrt2Scr(currY), xZero + MidTickSz, yCrt2Scr(currY))
                    }
                    else
                    {
                        color = SmallTickClr
                        drawLine(xZero - SmallTickSz, yCrt2Scr(currY), xZero + SmallTickSz, yCrt2Scr(currY))
                    }
                    currY += 0.1
                    currY = (currY * 10).roundToInt() / 10.0 // Здесь оставляю только одну цифру после запятой
                }
                currY = firstWholeY // Теперь текущая ордината - первое целое число

                // В этом цикле отрисовываются одновременно большие, средние и маленькие тики
                while (currY <= lastWholeY) {
                    color = LargeTickClr
                    if ((currY * 10).toInt() != 0) {
                        drawLine(
                            xZero - LargeTickSz,
                            yCrt2Scr(currY),
                            xZero + LargeTickSz,
                            yCrt2Scr(currY)
                        )
                        wholePointsY.add(currY)
                    }
                    var k = 1
                    // Ведем цикл отрисовки маленьких и средних тиков
                    while (k <= 10)
                    {
                        if (k != 5)
                        {
                            color = SmallTickClr
                            drawLine(xZero - SmallTickSz, yCrt2Scr(currY + k * 0.1), xZero + SmallTickSz, yCrt2Scr(currY + k * 0.1))
                        }
                        else
                        {
                            color = MidTickClr
                            drawLine(xZero - MidTickSz, yCrt2Scr(currY + k * 0.1), xZero + MidTickSz, yCrt2Scr(currY + k * 0.1))
                        }
                        k++
                    }
                    currY += 1.0
                }
                drawYLabels(this, xZero)
            }
        }
    }

    private fun drawXLabels(g : Graphics, yZeroPoint : Int) {
        with (g as Graphics2D) {
            stroke = BasicStroke(1F)
            color = LargeTickClr
            font = mainFont
            with(plane) {
                for (i in 0 until wholePointsX.size) {
                    /*drawLine(xCrt2Scr(wholePointsX[i]), yCrt2Scr(0.0) - LargeTickSz, xCrt2Scr(wholePointsX[i]), yCrt2Scr(0.0) + LargeTickSz)
                    val tw = fontMetrics.getStringBounds(tickValue.toString(), g).width
                    val th = fontMetrics.getStringBounds(tickValue.toString(), g).height
                     Можем так записать благодаря тому, что Pair - data class*/
                    val (tW, tH) = with(fontMetrics.getStringBounds(wholePointsX[i].toString(), g)) {
                        Pair(width.toInt(), height.toInt())
                    }
                    if (yZeroPoint >= 0 && yZeroPoint < height)
                        drawString(wholePointsX[i].toString(), xCrt2Scr(wholePointsX[i]) - tW / 2, yZeroPoint + LargeTickSz + tH)
                    else
                        drawString(wholePointsX[i].toString(), xCrt2Scr(wholePointsX[i]) - tW / 2, yZeroPoint - LargeTickSz - tH)
                }
            }
        }
    }
    private fun drawYLabels(g : Graphics, xZeroPoint: Int) {
        with (g as Graphics2D) {
            stroke = BasicStroke(1F)
            color = LargeTickClr
            font = mainFont
            with(plane) {
                for (i in 0 until wholePointsY.size) {
                    /* drawLine(xCrt2Scr(0.0) - LargeTickSz, yCrt2Scr(wholePointsY[i]), xCrt2Scr(0.0) + LargeTickSz, yCrt2Scr(0.0) + LargeTickSz)
                    val tw = fontMetrics.getStringBounds(tickValue.toString(), g).width
                    val th = fontMetrics.getStringBounds(tickValue.toString(), g).height
                    */
                    //Можем так записать благодаря тому, что Pair - data class
                    val (tW, tH) = with(fontMetrics.getStringBounds(wholePointsY[i].toString(), g)) {
                        Pair(width.toInt(), height.toInt())
                    }
                    if (xZeroPoint >= 0 && xZeroPoint < width)
                        drawString(wholePointsY[i].toString(), xZeroPoint + LargeTickSz + tW / 4, yCrt2Scr(wholePointsY[i]) + tH / 4)
                    else
                        drawString(wholePointsY[i].toString(), xZeroPoint - LargeTickSz*2 - tW , yCrt2Scr(wholePointsY[i]) + tH / 4)
                }
            }
        }
    }

}


