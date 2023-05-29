import multiStepMethod.impl.MilnaMethod
import oneStepMethod.impl.EulerMethod
import oneStepMethod.impl.RungeKuttaMethod
import org.jetbrains.letsPlot.LetsPlot
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.themes.themeGrey
import utils.Result
import java.io.*
import java.time.LocalDateTime
import kotlin.math.*

object CLI {

    private lateinit var input: () -> String
    private lateinit var bw: BufferedWriter
    private val br = BufferedReader(FileReader("src/files/tasks/task3.txt"))
    private var visible = true
    private var equations = mapOf(
        1 to DiffEquation(
            { x: Double, y: Double -> y + (1 + x) * y * y },
            { x: Double, C: Double -> -(exp(x) / (x * exp(x) + C)) },
            { x: Double, y: Double -> y + exp(x) / (x * exp(x)) },
            "y' = y + (1 + x) * y^2"
        ),
        2 to DiffEquation(
            { x: Double, y: Double -> -(2 * y + 1) * cos(x) },
            { x: Double, C: Double -> C / exp(2 * sin(x)) - 0.5 },
            { x: Double, y: Double -> (y + 0.5) * exp(2 * sin(x)) },
            "y' = -(2y + 1) * cos(x)"
        ),
        3 to DiffEquation(
            { x: Double, y: Double -> x.pow(5)},
            { x: Double, C: Double -> x.pow(6)/6 + C},
            { x: Double, y: Double -> (y * 6 -  x.pow(6))/6},
            "y' = x^5"
        )
    )


    fun solveEquation() {
        askInputOption();
        val diffEquation = askEquation()
        val (x0, y0) = askInitialConditions()
        val (xo, xn) = askInterval()
        val h = askH(xn - xo)
        val n = floor((xn - xo) / h).toInt() + 1
        val e = askE()
        askOutputOption()
        val c = diffEquation.c(x0, y0)
        val eulerMethod = EulerMethod.solve(diffEquation.f, xo, xn, y0, h, n, e)
        val rungeKuttaMethod = RungeKuttaMethod.solve(diffEquation.f, xo, xn, y0, eulerMethod.h!!, n, e)
        val milnaMethod = MilnaMethod.solve(diffEquation.f, xo, xn, y0, eulerMethod.h, n, e)
        printResult(diffEquation.answerF, eulerMethod, rungeKuttaMethod, milnaMethod, c)
        bw.close()
    }


    private fun printResult(
        answer: (x: Double, C: Double) -> Double,
        euler: Result,
        rungeKutta: Result,
        milna: Result,
        c: Double
    ) {
        val realAnswer = ArrayList(euler.x.map { answer(it, c) })
        printTable(euler, "Метод Эйлера")
        printTable(rungeKutta, "Метод Рунге-Кутта")
        printTable(milna, "Метод Милна")
        printTable(Result(euler.x, realAnswer, null), "Правильный ответ")
        drawPlot(Result(euler.x, realAnswer, null) , euler, rungeKutta, milna)

    }


    private fun drawPlot(
        real: Result,
        euler: Result,
        rungeKutta: Result,
        milna: Result,
    ) {
        val data = mapOf(
            real.x to real.y, euler.x to euler.y, rungeKutta.x to rungeKutta.y, milna.x to milna.y,
        )

        LetsPlot.theme = themeGrey()
        val plot = letsPlot(data) +
                geomLine(color = "red") { x = real.x; y = real.y } +
                geomLine(color = "black") { x = euler.x; y = euler.y } +
                geomLine(color = "blue") { x = rungeKutta.x; y = rungeKutta.y } +
                geomLine(color = "yellow") { x = milna.x; y = milna.y}


        plot.show()
    }

    private fun printTable(result: Result, nameOfMethod: String) {
        bw.write("-----------------------------------")
        bw.newLine()
        bw.write(nameOfMethod)
        bw.newLine()
        bw.write("xi    yi")
        bw.newLine()
        result.x.zip(result.y).forEach {
            bw.write("${String.format("%.3f", it.first)} ${String.format("%.10f", it.second)}")
            bw.newLine()
        }
        bw.write("-----------------------------------")
        bw.newLine()

    }


    private fun askOutputOption() {
        print("Записать результат в файл? Д/н ")
        val str = readln()
        bw = when (str.lowercase()) {
            "д", "\n", "l" -> {
                createFileAndWriteResult()
            }

            else -> {
                BufferedWriter(OutputStreamWriter(System.out))
            }
        }
    }

    private fun askInputOption() {
        print("Прочитать данные из файла? Д/н ")
        val str = readln()
        when (str.lowercase()) {
            "д", "\n", "l" -> {
                visible = false
                input = { br.readLine() }
            }

            else -> {
                visible = true
                input = { readln() }
            }
        }
    }


    private fun askEquation(): DiffEquation {
        equations.forEach {
            ask("${it.key}) ${it.value.textFunction}\n")
        }
        while (true) {
            ask("Введите номер дифференциального уравнения, корень которого надо найти: ")
            val index = input().toIntOrNull()
            if (index != null && index > 0 && index <= equations.size) {
                return equations[index]!!
            }
            ask("Номер уравнения должен быть представлен числом и быть в пределах [1; ${equations.size}]\n")
        }
    }

    private fun askInitialConditions(): Pair<Double, Double> {
        askln("Введите начальные условия: ")
        val x0 = askX0();
        return x0 to askY0(x0)
    }

    private fun askX0(): Double {
        while (true) {
            ask("x0 = ")
            val x0 = input().toDoubleOrNull()
            if (x0 != null) return x0
            askln("x0 должен быть представлен числом")
        }
    }

    private fun askE(): Double {
        while (true) {
            ask("Введите точность: ")
            val e = input().toDoubleOrNull()
            if (e != null) return e
            askln("Точность должна быть представлена числом!")
        }
    }


    private fun askY0(x0: Double): Double {
        while (true) {
            ask("y0[$x0] = ")
            val y0 = input().toDoubleOrNull()
            if (y0 != null) return y0
            askln("y0 должен быть представлен числом")
        }
    }

    private fun askH(length: Double): Double {
        while (true) {
            ask("Введите шаг: ")
            val h = input().toDoubleOrNull()
            if (h != null && h <= length) return h
            askln("Шаг должен быть представлен числом и быть не больше, чем разность между точками интервеала дифференциирования")
        }

    }

    private fun askInterval(): Pair<Double, Double> {
        while (true) {
            try {
                ask("Введите интервал дифференциирования через пробел: ")
                val (a, b) = input().split(" ").map { it.toDoubleOrNull() }
                if (a is Double && b is Double) return Pair(min(a, b), max(a, b))
            } catch (e: IndexOutOfBoundsException) {
                ask("Введите через пробел два числа!\n")
            }

        }
    }


    private fun ask(text: String) {
        if (visible) print(text)
    }

    private fun askln(text: String? = null) {
        if (visible) println(text)
    }

    private fun createFileAndWriteResult(): BufferedWriter {
        val date = LocalDateTime.now()
        val file = File(
            "/home/newton/IdeaProjects/Math/comp_math/lab4/src/files/results/" + "${date.dayOfMonth}_${date.month}_${date.hour}:${date.minute}.${date.second}"
        )

        return BufferedWriter(FileWriter(file))
    }


}