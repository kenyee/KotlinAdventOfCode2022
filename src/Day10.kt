fun main() { // ktlint-disable filename

    fun part1(input: List<String>): Long {
        var registerX = 1
        val signalStrengths = mutableListOf<Long>()
        var cycle = 1

        fun checkCycleStrengths(startCheckCycle: Int = 20, cycleCheckPoint: Int = 40) {
            if ((cycle - startCheckCycle).rem(cycleCheckPoint) == 0) {
                // println("Cycle:$cycle X:$registerX")
                signalStrengths.add(cycle * registerX.toLong())
            }
        }

        input.forEach { line ->
            val tokens = line.split(" ")
            when (tokens[0]) {
                "addx" -> {
                    cycle++
                    checkCycleStrengths()
                    registerX += tokens[1].toInt()
                    cycle++
                    checkCycleStrengths()
                }
                "noop" -> {
                    cycle++
                    checkCycleStrengths()
                }
            }
        }
        return signalStrengths.sum()
    }

    fun part2(input: List<String>): List<String> {
        var registerX = 1
        var cycle = 1
        val crtLine = StringBuilder()
        val crtOutput = mutableListOf<String>()
        val cyclesPerLine = 40

        fun addCrtPoint() {
            val xPos = (cycle - 1).rem(cyclesPerLine)
            crtLine.append(
                if (xPos >= registerX - 1 && xPos <= registerX + 1) '⚪' else '⚫'
            )
            if (xPos == cyclesPerLine - 1) {
                crtOutput.add(crtLine.toString())
                crtLine.clear()
            }
        }

        input.forEach { line ->
            val tokens = line.split(" ")
            when (tokens[0]) {
                "addx" -> {
                    addCrtPoint()
                    cycle++
                    addCrtPoint()
                    cycle++
                    registerX += tokens[1].toInt()
                }
                "noop" -> {
                    addCrtPoint()
                    cycle++
                }
            }
        }
        return crtOutput
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    println("Test sum of signal strengths: ${part1(testInput)}")
    check(part1(testInput) == 13140L)
    println("Test CRT output:\n${part2(testInput).joinToString("\n")}")
    // check(part2(testInput) == 1)

    val input = readInput("Day10_input")
    println("Sum of signal strengths: ${part1(input)}")
    println("CRT output:\n${part2(input).joinToString("\n")}")
}
