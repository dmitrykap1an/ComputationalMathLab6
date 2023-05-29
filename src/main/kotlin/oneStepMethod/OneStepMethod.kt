package oneStepMethod

import utils.Result

interface OneStepMethod {

    fun solve(
        f: (x: Double, y: Double) -> Double,
        x0: Double,
        xn: Double,
        y0: Double,
        h: Double,
        n: Int,
        e: Double,
        cnt: Int = 0
    ): Result
}