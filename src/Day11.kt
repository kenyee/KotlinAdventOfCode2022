import java.math.BigInteger

interface Operation {
    fun exec(input: Long): Long
}
class Multiply(private val multiplier: Long) : Operation {
    override fun exec(input: Long): Long {
        return input * multiplier
    }
}
class Add(private val addValue: Long) : Operation {
    override fun exec(input: Long): Long {
        return input + addValue
    }
}
class Squared : Operation {
    override fun exec(input: Long): Long {
        return input * input
    }
}

data class Monkey(
    val itemWorryLevel: MutableList<Long>,
    val operation: (Long) -> Long,
    val shouldThrow: (Long) -> Boolean,
    val testDivisor: Int,
    val trueDest: Int,
    val falseDest: Int,
    var numInspections: Long = 0
)

fun main() { // ktlint-disable filename

    fun calcMonkeyInspections(input: List<String>, numRounds: Int, worryDivisor: Int): Long {
        val monkeys = mutableListOf<Monkey>()
        val opRegex = """Operation: new = old ([\+\*]) (old)?(\d+)?""".toRegex()
        val testRegex = """Test: divisible by (\d+)""".toRegex()
        input.windowed(7, step = 7, partialWindows = true).forEach { monkeyNotes ->
            val itemsList = monkeyNotes[1].split("items:")[1].split(",").map { it.trim().toLong() }
            val (_, op, old, opValue) = opRegex.find(monkeyNotes[2])!!.groupValues
            val testResult = testRegex.find(monkeyNotes[3])!!.groupValues[1].toInt()
            val trueResult = monkeyNotes[4].substring(monkeyNotes[4].lastIndexOf(" ") + 1).toInt()
            val falseResult = monkeyNotes[5].substring(monkeyNotes[5].lastIndexOf(" ") + 1).toInt()

            // NOTE: causes a backend compiler error w/ Kotlin 1.7.22 if you use the commented out code
            var operation = Squared()::exec
            if (old == "") operation = Multiply(opValue.toLong())::exec
            if (op == "+") operation = Add(opValue.toLong())::exec
//                val operation = when (op) {
//                    "*" -> {
//                        if (old == "old") Squared()::exec else Multiply(opValue.toInt())::exec
//                    }
//                    "+" -> {
//                        Add(opValue.toInt())::exec
//                    }
//                    else -> {
//                        throw IllegalArgumentException("Unexpected op: $op")
//                    }
//                }

            monkeys.add(
                Monkey(
                    itemWorryLevel = itemsList.toMutableList(),
                    operation = operation,
                    shouldThrow = { value -> value.rem(testResult) == 0L },
                    testDivisor = testResult,
                    trueDest = trueResult,
                    falseDest = falseResult
                )
            )
        }

        val testLCM = monkeys.map { BigInteger.valueOf(it.testDivisor.toLong()) }
            .reduce { acc, divisor -> acc.lcm(divisor) }.toLong()

        repeat(numRounds) {
            monkeys.forEach { monkey ->
                monkey.itemWorryLevel.forEach { level ->
                    val newValue = if (worryDivisor == -1) {
                        monkey.operation(level).rem(testLCM)
                    } else {
                        monkey.operation(level) / worryDivisor
                    }
                    if (monkey.shouldThrow(newValue)) {
                        monkeys[monkey.trueDest].itemWorryLevel.add(newValue)
                    } else {
                        monkeys[monkey.falseDest].itemWorryLevel.add(newValue)
                    }
                    monkey.numInspections++
                }
                monkey.itemWorryLevel.clear()
            }
        }
        val topMonkeys = monkeys.sortedByDescending { it.numInspections }
        // println("top monkeys: $topMonkeys")
        val topTwoInspectors = topMonkeys.take(2)
        return topTwoInspectors[0].numInspections * topTwoInspectors[1].numInspections
    }

    fun part1(input: List<String>): Long {
        return calcMonkeyInspections(input, numRounds = 20, worryDivisor = 3)
    }

    fun part2(input: List<String>): Long {
        return calcMonkeyInspections(input, numRounds = 10000, worryDivisor = -1)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    println("Test 20 rounds Top2 inspection count product: ${part1(testInput)}")
    check(part1(testInput) == 10605L)
    println("Test 10K rounds Top2 inspection count product: ${part2(testInput)}")
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11_input")
    println("Top2 20 rounds inspection count product: ${part1(input)}")
    println("Top2 10K rounds inspection count product: ${part2(input)}")
}
