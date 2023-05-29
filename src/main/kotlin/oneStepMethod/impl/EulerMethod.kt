package oneStepMethod.impl

import oneStepMethod.OneStepMethod
import kotlin.math.abs
import utils.Result

object EulerMethod : OneStepMethod {
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
        val yh2 = euler(f, x0, xn, y0, h, n)
        val yh = euler(f, x0, xn, y0, h/2, n)

        for(i in 0 until n){
            if(cnt > 100) break
            if(abs(yh2.y[i] - yh.y[i])/15 > e) return solve(f, x0, xn, y0, h/2, n, e, cnt + 1)
        }
        return yh2
    }

    private fun euler(
        f: (x: Double, y: Double) -> Double,
        x0: Double,
        xn: Double,
        y0: Double,
        h: Double,
        n: Int,
    ): Result {
        val X = ArrayList<Double>()
        val Y = ArrayList<Double>()
        var y = y0
        var x = x0
        while(x <= xn + h) {
            X.add(x)
            Y.add(y)
            y += h * f(x, y)
            x += h
        }
        return Result(X, Y, h)
    }


}