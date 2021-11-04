package ru.amaks

import ru.amaks.math.polynom.Lagrange
import ru.amaks.math.polynom.Newton
import ru.amaks.math.polynom.Polynom
import ru.amaks.ui.PolynomUI

fun main() {
  /*  val n1 = Newton(mutableMapOf(-1.0 to 1.0, 0.0 to 0.0, 1.0 to 1.0))
    //println(n1)
    val n2 = Newton(mutableMapOf(1.0 to 1.0, 0.0 to -1.0, 0.5 to 1.0))
    var v1 = System.currentTimeMillis()
    n1.addPoint(2.0, 16.0)
    n1.addPoint(3.0, 81.0)
    var v2 = System.currentTimeMillis()
    println(v2 - v1)
    v1 = System.currentTimeMillis()
    val l2 = Lagrange(mapOf(1.0 to 1.0, 0.0 to -1.0, 0.5 to 1.0, 2.0 to 16.0, 3.0 to 81.0))
    v2 = System.currentTimeMillis()
    println(v2 - v1)
    val n = Polynom(3.0)
    println(n)
    print(n.find_Derivative())*/
    PolynomUI().isVisible = true
}


