package ru.amaks.math.polynom

import kotlin.math.abs
import ru.amaks.math.le

class Newton (private val points: MutableMap<Double, Double>): Polynom() {
    // добавить метод add, для добавления одного узла (то есть не пересчитывать)
    // y1 + (x-x1)f(x1,x2)+...
    val _points : MutableMap<Double, Double>
        get () = points
    val nodes = points.keys.toMutableList() // Узлы (используем mutableList, т.к. с этим списком будут производиться изменения в find_diff)
    val div_diffs = mutableMapOf<String, Double>() // Словарь для хранения разностных отношений, в качестве ключей - строки
    var curr_polValue = Polynom()// Текущее значение полинома; использую для метода add, чтобы не пересчитывать весь полином
    val curr_multValue : Polynom// Текущее значение произведения (x-x_1)...(x-x_n)
    init {
        if (!points.isEmpty()) {
            curr_multValue = Polynom(listOf(-points.keys.first(), 1.0))
            findDiff(nodes) // В этой функции рекурсивно подсчитываются разностные отношения и добавляются в div_diffs
            val nton = Polynom(points.values.first()) // Полином, накапливающий значение, использую в цикле
            var curr_Diff = 0.0 // Значение текущего разностного отношения                                        s
            for (i in 1 until nodes.size) { // Цикл, в котором вычисляется сумма L_n(x) = f(x_1) + (x - x_1)*f(x_1, x_2) + ...
                curr_Diff = div_diffs.getValue(nodes.subList(0, i + 1).joinToString(","))
                nton += curr_multValue * curr_Diff
                curr_multValue *= Polynom(
                    listOf(
                        -nodes[i],
                        1.0
                    )
                ) // Умножаем на следующий полином вида (x-x_i), где i - номер узла
            }
            curr_polValue = nton
            coeff = curr_polValue.coeff
        }
        else {
            curr_multValue = Polynom(1.0)
            curr_polValue = Polynom()
            coeff = curr_polValue.coeff
        }
    }

    private fun findDiff(xPoints: MutableList<Double>): Double {   // Можно усовершенствовать, не пробегая по значениям, которые уже вычислены
        if (xPoints.size == 1) points.keys.forEach {
            if (it == xPoints.first()) return points[it]!!      // NULL не будет!
        }
        // Рекурсивно считаем f(x_2, ..., x_n) - f(x_1, ..., x_(n-1)) / (x_n - x_1)
        val k = (findDiff(xPoints.subList(1, xPoints.size)) - findDiff(xPoints.subList(0, xPoints.size-1))) / (xPoints[xPoints.size - 1] - xPoints[0])
        // Добавляем ключ-значение в MutableMap разностных отношений
        if (!div_diffs.containsKey(xPoints.joinToString(""))) div_diffs.put(xPoints.joinToString(","), k)
        return k
    }
    // Функция добавления нового узла
    public fun addPoint (pointX : Double, pointY : Double) {
        // L_(n+1) = L_n + (x-x_1)...(x-x_n) * f(x_1, ..., x_(n+1))
        // f(x_1, ..., x_(n+1)) = f(x_2, ..., x_(n+1)) - f(x_1, ..., x_n) / ( x_(n+1) - x_1 )

        points.put(pointX, pointY)    // Добавляем в points новую точку
        nodes.add(pointX)   // Также добавляем новый узел в список узлов

        // Сохраняем значение последнего посчитанного разностного, т.к. не хотим заново его пересчитывать рекурсивно
       // Будем использовать это значение для вычисления разностного отношения f(x_1, ..., x_n, x_new), где x_new - новый узел
        // Таким образом lastDiff = f(x_1, ..., x_n)

        val lastDiff = if (!div_diffs.isEmpty()) div_diffs.values.last() else points.values.first()
       //  Значение разностного отношения вида f(x_2,..., x_new)
        val newDiff = if (nodes.size > 1) findDiff(nodes.subList(1, nodes.size)) else points.values.first()

        // newAddition = (x-x_1)...(x-x_n) *  [f(x_2,..., x_new) -  f(x_1, ..., x_n)] / (x_new - x_1)
        // Т.е. newAddition = (x-x_1)...(x-x_n) *  f(x_1, ..., x_n, x_new)
        val newAddition = if (nodes.size > 1) curr_multValue * ((newDiff - lastDiff ) / (pointX - nodes[0])) else  Polynom(points.values.first())

        // Кладем в словарь новое разностное отношение, при добавлении следующего нового узла это разностное отношение будет последним
        if (nodes.size > 1)
            div_diffs.put(nodes.joinToString(","),(newDiff - lastDiff ) / (pointX - nodes[0]))
        else if (nodes.size == 1)  div_diffs.put(nodes[0].toString(),newDiff)
        curr_polValue = curr_polValue + newAddition// Почему нельзя +=?
        coeff = curr_polValue.coeff
        curr_multValue *= Polynom(listOf(-pointX, 1.0))  // Изменяем текущее произведение, для использования в новом узле (если он будет добавлен)
    }
    public fun deletePointX (pointX : Double, epsilon : Double) {
        points.keys.forEach{
            if (abs(pointX - it) le epsilon) {
                points.remove(pointX)
                return
            }
        }

    }
}