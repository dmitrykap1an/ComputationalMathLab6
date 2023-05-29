package oneStepMethod.impl

import oneStepMethod.OneStepMethod
import utils.Result
import kotlin.math.abs

object RungeKuttaMethod : OneStepMethod {

    override fun solve(
        f: (x: Double, y: Double) -> Double,
        x0: Double,
        xn: Double,
        y0: Double,
        h: Double,
        n: Int,
        e: Double,
        cnt: Int
    ): Result {
        val yh2 = rungeKutta(f, x0, xn, y0, h)
        val yh = rungeKutta(f, x0, xn, y0, h / 2)
        for(i in 0 until yh2.y.size){
            if(cnt > 100) break
            if(abs(yh2.y[i] - yh.y[i]) /15 > e + 0.1) return solve(f, x0, xn, y0, h / 2, n, e, cnt + 1)
        }
        return yh2
    }

    private fun rungeKutta(
        f: (x: Double, y: Double) -> Double,
        x0: Double,
        xn: Double,
        y0: Double,
        h: Double,
    ): Result {
        val X = ArrayList<Double>()
        val Y = ArrayList<Double>()
        var y = y0
        var x = x0
        while(x <= xn + h){
            Y.add(y)
            X.add(x)
            val k1 = h * f(x, y)
            val k2 = h * f(x + h/2, y + k1/2)
            val k3 = h * f(x + h/2, y + k2/2)
            val k4 = h * f(x + h, y + k3)
            x += h
            y+= (k1 + 2 * k2 + 2 * k3 + k4)/6
        }

        return Result(X, Y, h)
    }

    fun solveN(
        f: (x: Double, y: Double) -> Double,
        x0: Double,
        y0: Double,
        h: Double,
        n: Int,
    ): Result {
        val X = ArrayList<Double>()
        val Y = ArrayList<Double>()
        var y = y0
        var x = x0
        repeat(n){
            Y.add(y)
            X.add(x)
            val k1 = h * f(x, y)
            val k2 = h * f(x + h/2, y + k1/2)
            val k3 = h * f(x + h/2, y + k2/2)
            val k4 = h * f(x + h, y + k3)
            x += h
            y+= (k1 + 2 * k2 + 2 * k3 + k4)/6
        }

        return Result(X, Y, h)
    }



}