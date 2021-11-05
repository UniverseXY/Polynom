package ru.amaks.math.polynom

import ru.amaks.math.eq
import ru.amaks.math.le
import kotlin.math.abs

class Newton_2(private val points: MutableMap<Double, Double>): Polynom() {

    private var pr:Polynom = Polynom(1.0)
    val _points : MutableMap<Double, Double>
        get () = points
    init {
        val newt = Polynom()
        val k = points.keys.toList()
        val v = points.values.toList()
        if (k.isNotEmpty()) {
            newt += v[0]
            for (i in 0..(k.size - 2)) {
                pr = pr * Polynom(mutableListOf(-k[i], 1.0))
                newt += pr * f(i + 1)
            }
            coeff = newt.coeff
            pr =pr *  Polynom(mutableListOf(-k[k.size - 1], 1.0))
        }
    }

    private fun f(n: Int): Double{
        val k = points.keys.toList()
        val v = points.values.toList()
        var res = 0.0
        for (j in 0..n){
            var p = 1.0
            for (i in 0..n){
                if (j != i) p *= (k[j] - k[i])
            }
            res += v[j] / p
        }
        return res
    }

    fun addPoint(x: Double, y: Double){
        points[x] = y
        this += pr * f(points.size - 1)
        pr =pr * Polynom(mutableListOf(-x, 1.0))
    }
    fun deletePointX (pointX : Double) {
        val keys = _points.keys
       /* keys.forEach{
            if (abs(pointX - it) eq 0.0 ) {
                points.remove(pointX)
            }
        }*/
        keys.removeIf{abs(pointX - it) eq 0.0}
        val changedPolynom = Newton_2(points)
        this.pr = changedPolynom.pr
        this.coeff = changedPolynom.coeff

    }
}