package multiStepMethod

interface MultistepMethod {

  fun solve(f: (x: Double, y: Double) -> Double, x0: Double, y0: Double, h: Double, n: Int, e: Double): Pair<ArrayList<Double>, ArrayList<Double>>

}