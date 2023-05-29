package multiStepMethod.impl

import multiStepMethod.MultistepMethod
import oneStepMethod.impl.RungeKuttaMethod
import kotlin.math.abs
import utils.Result
import kotlin.math.floor

object MilnaMethod : MultistepMethod {
    override fun solve(
        f: (x: Double, y: Double) -> Double,
        x0: Double,
        xn: Double,
        y0: Double,
        h: Double,
        n: Int,
        e: Double
    ): Result{

        val (x, y) = RungeKuttaMethod.solveN(f, x0, y0, h, 4)
        val newN = floor((xn - x0)/h).toInt() + 1
        val fValues = ArrayList(x.zip(y).drop(1).map { f(it.first, it.second) })
        for (i in 4 until newN) {
            val xi = x.last() + h
            val predictedY = y[y.size - 4] + 4 * h /3 * (2 * fValues[fValues.size - 3] - fValues[fValues.size - 2] + 2 * fValues[fValues.size - 1])
            var fi = f(xi, predictedY)
            var correctionalY = y[y.size - 2] + h/3 * (fValues[fValues.size - 2] + 4 * fValues[fValues.size - 1] + fi)
            while(abs(correctionalY - predictedY) >= e) {
                fi = f(xi, predictedY)
                correctionalY = y[y.size - 2] + h/3 * (fValues[fValues.size - 2] + 4 * fValues[fValues.size - 1] + fi)
            }

            x.add(xi)
            y.add(correctionalY)
            fValues.add(fi)
        }
        return Result(x, y, null)
    }

}