package oneStepMethod.impl

import oneStepMethod.OneStepMethod
import kotlin.math.abs

object RungeKuttaMethod : OneStepMethod {
    override fun solve(
        f: (x: Double, y: Double) -> Double,
        x0: Double,
        y0: Double,
        h: Double,
        n: Int,
        e: Double
    ): Pair<ArrayList<Double>, ArrayList<Double>> {
        val yh2 = rungeKutta(f, x0, y0, h, n)
        val yh = rungeKutta(f, x0, y0, h / 2, n)
        for(i in 0 until yh.second.size){
            if(abs(yh2.second[i] - yh.second[i]) /15 > e) return solve(f, x0, y0, h / 2, n, e)
        }
        return yh2
    }

    private fun rungeKutta(
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
        for(i in 1..n){
            Y.add(y)
            X.add(x)
            val k1 = h * f(x, y)
            val k2 = h * f(x + h/2, y + k1/2)
            val k3 = h * f(x + h/2, y + k2/2)
            val k4 = h * f(x + h, y + k3)
            x += h
            y+= (k1 + 2 * k2 + 2 * k3 + k4)/6
        }

        return X to Y
    }
}