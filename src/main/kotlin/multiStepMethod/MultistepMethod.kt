package multiStepMethod

import utils.Result

interface MultistepMethod {

  fun solve(f: (x: Double, y: Double) -> Double, x0: Double, xn: Double, y0: Double, h: Double, n: Int, e: Double): Result

}