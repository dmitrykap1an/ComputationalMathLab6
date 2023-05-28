data class DiffEquation(
    val f: (x: Double, y: Double) -> Double,
    val answerF: (x: Double, C: Double) -> Double,
    val c: (x: Double, y: Double) -> Double,
    val textFunction: String
)