package oneStepMethod.impl

import oneStepMethod.OneStepMethod
import kotlin.math.abs

object EulerMethod : OneStepMethod {
    override fun solve(
        f: (x: Double, y: Double) -> Double,
        x0: Double,
        y0: Double,
        h: Double,
        n: Int,
        e: Double
    ): Pair<ArrayList<Double>, ArrayList<Double>> {
        val yh2 = euler(f, x0, y0, h, n)
        val yh = euler(f, x0, y0, h/2, n)
        for(i in 0 until yh.second.size){
            if(abs(yh2.second[i] - yh.second[i])/15 > e) return solve(f, x0, y0, h/2, n, e)
        }
        return yh2
    }

    private fun euler(
        f: (x: Double, y: Double) -> Double,
        x0: Double,
        y0: Double,
        h: Double,
        n: Int,
    ): Pair<ArrayList<Double>, ArrayList<Double>> {
        val X = ArrayList<Double>()
        val Y = ArrayList<Double>()
        var y = y0
        var x = x0
        for (i in 1..n) {
            X.add(x)
            Y.add(y)
            y += h * f(x, y)
            x += h
        }
        return X to Y
    }


}