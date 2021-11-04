package ru.amaks.math.polynom

    class Lagrange (private val points: Map<Double, Double>) : Polynom() {

    init {
        val lagr = Polynom()
        points.keys.forEach { lagr += create_fundamental(it) * points[it]!! } // Ставим !!, если 100% не null Альтернатива (points[it] ?: 0.0)
        coeff = lagr.coeff
    }

        private fun create_fundamental(x: Double): Polynom = Polynom(1.0).apply {
            points.keys.forEach{ if (x != it) this *= Polynom(listOf(-it, 1.0)) / (x - it)}
        }

}
