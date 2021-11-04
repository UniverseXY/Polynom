package ru.amaks.math.polynom
import java.lang.StringBuilder
import kotlin.math.abs
import ru.amaks.math.*
import java.lang.Math.pow
import kotlin.math.max

open class Polynom(coeff: Collection<Double>) {

    /**
     * Набор коэффициентов, доступных внутри класса
     */
    private val _coeff: MutableList<Double>

    /**
     * Неизменяемый список коэффициентов для получения извне
     */
    var coeff: List<Double>
        get() = _coeff.toList()
        protected set(value) // Изменения только внутри класса
        {
            _coeff.clear()
            _coeff.addAll(value)
        }
    /**
     * Степень полинома
     */
    val degree: Int
        get() = coeff.size - 1


    init{
        _coeff = coeff.toMutableList()
        removeZeros()
    }

    constructor(coeff: Array<Double>) : this(coeff.toList())
    //constructor(vararg coeff: Double): this(coeff.toList()) //Переводим coeff в тип DoubleArray, для произвольного количества параметров
    constructor(coeff: DoubleArray): this(coeff.toList())
    constructor(coeff: Double) : this(listOf(coeff))

    /**
     * Создание полинома нулевой степени с коэффициентом = 0
     */
        constructor() : this(mutableListOf(0.0))

    /**
     * Для корректного определения степени, убираем нули при старших степенях полинома, если они есть
     */
    private fun removeZeros(){
        var found = false
        val nc = coeff.reversed().filter{
            if (found || it neq 0.0) {found = true; true} else false
        }
        _coeff.clear()
        _coeff.addAll(nc.reversed())
        if (_coeff.size == 0) _coeff.add(0.0)
    }

    operator fun plus(other: Polynom) =
        Polynom(DoubleArray(max(degree, other.degree)+1){
            (if (it <= degree) _coeff[it] else 0.0) +

                    if (it <= other.degree) other._coeff[it] else 0.0
        })

    operator fun plus(value: Double)  =
        Polynom(
            DoubleArray(degree+1){ _coeff[it] + if (it == 0) value else 0.0}
        )

    operator fun plusAssign(other: Polynom){
        _coeff.indices.forEach {
            _coeff[it] += if (it <= other.degree) other._coeff[it] else 0.0
        }
        for (i in degree+1..other.degree){
            _coeff.add(other._coeff[i])
        }
        removeZeros()
    }

    operator fun plusAssign(value: Double){
        _coeff[0] += value
    }

    operator fun minus(other: Polynom) = this + other * -1.0

    operator fun minus(value: Double) = this + value * -1.0

    operator fun minusAssign(other: Polynom){
        this.plusAssign(other)
    }

    operator fun minusAssign(value: Double){
        this.plusAssign(-value)
    }

    operator fun times(value: Double) =
        Polynom(DoubleArray(degree + 1){_coeff[it] * value})

    operator fun timesAssign(value: Double){
        coeff = _coeff.map{it * value}
        removeZeros()
    }

    operator fun times (other: Polynom) : Polynom  {
        val nArr = DoubleArray(degree + other.degree + 1)
        for (i in 0..degree)
            for (j in 0..other.degree)
                    nArr[i+j] += _coeff[i] * other._coeff[j]
        return Polynom(nArr)
    }

    public fun find_Derivative() = Polynom(DoubleArray(degree){
        _coeff[it+1] * (it + 1)
    })
    operator fun timesAssign (other: Polynom) {
      coeff = (this * other)._coeff
    }

    operator fun div(value: Double) =
        Polynom(DoubleArray(degree+1){  _coeff[it] / value })

    override fun equals (other: Any?) : Boolean =
        (other is Polynom) && (_coeff == other._coeff)

    override fun hashCode(): Int { // Одинаковый хеш-код для одинаковых полиномов
        return _coeff.hashCode() // Если бы помимо коэффициентов были другие ращличительные признаки, то надо было бы удостовериться
    }
    operator fun divAssign(value: Double){
        _coeff.indices.forEach { _coeff[it] /= value }
    }
    operator fun unaryMinus() = this * -1.0

    operator fun invoke(x: Double): Double {
        var p = 1.0;
        return  _coeff.reduce { res, coef -> p *= x; res + coef * p }
    }
    override fun toString() =
        coeff.indices.reversed().joinToString(""){ ind ->
            val monStr = StringBuilder()
            val acoeff = abs(coeff[ind])
            if (coeff[ind] neq 0.0){
                if (ind < coeff.size - 1 && coeff[ind] > 0){
                    monStr.append("+")
                } else if (coeff[ind] < 0) monStr.append("-")
                if (acoeff neq 1.0 || ind == 0)
                    if (abs(acoeff - acoeff.toInt().toDouble()) eq 0.0)
                        monStr.append(acoeff.toInt())
                    else monStr.append(acoeff)
                if (ind > 0) {
                    monStr.append("x")
                    if (ind > 1) monStr.append("^$ind")
                }
            } else {
                if (coeff.size == 1) monStr.append("0")
            }
            monStr
        }
}