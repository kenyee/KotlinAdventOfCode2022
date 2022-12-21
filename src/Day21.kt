import java.lang.IllegalArgumentException

sealed class MonkeyNode(val name: String) {
    class ShoutMonkey(name: String, private val value: Long) : MonkeyNode(name) {
        override fun compute(monkeys: Map<String, MonkeyNode>): Long {
            return value
        }
    }

    class CalcMonkey(
        name: String,
        private val left: String,
        private val right: String,
        val calc: (x: Long, y: Long) -> Long
    ) : MonkeyNode(name) {
        override fun compute(
            monkeys: Map<String, MonkeyNode>
        ): Long {
            return calc(monkeys[left]!!.compute(monkeys), monkeys[right]!!.compute(monkeys))
        }
    }

    abstract fun compute(monkeys: Map<String, MonkeyNode>): Long
}

fun main() { // ktlint-disable filename
    fun part1(input: List<String>): Long {
        val monkeys = mutableMapOf<String, MonkeyNode>()
        for (line in input) {
            val tokens = line.split(" ")
            val name = tokens[0].trimEnd(':')
            if (tokens.size == 2) {
                monkeys[name] = MonkeyNode.ShoutMonkey(name, tokens[1].toLong())
            } else {
                monkeys[name] = when (tokens[2]) {
                    "+" -> MonkeyNode.CalcMonkey(name, tokens[1], tokens[3]) { x, y -> x + y }
                    "-" -> MonkeyNode.CalcMonkey(name, tokens[1], tokens[3]) { x, y -> x - y }
                    "*" -> MonkeyNode.CalcMonkey(name, tokens[1], tokens[3]) { x, y -> x * y }
                    "/" -> MonkeyNode.CalcMonkey(name, tokens[1], tokens[3]) { x, y -> x / y }
                    else -> throw IllegalArgumentException("Unknown token $line")
                }
            }
        }

        return monkeys["root"]!!.compute(monkeys)
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    println("Test root monkey yells: ${part1(testInput)}")
    check(part1(testInput) == 152L)
//    println("Test #Exterior sides: ${part2(testInput)}")
//    check(part2(testInput) == 301)

    val input = readInput("Day21_input")
    println("root monkey yells: ${part1(input)}")
//    println("#Exterior sides: ${part2(input)}")
}
