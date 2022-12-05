
fun main() { // ktlint-disable filename

    fun containedRangesInPair(line: String): Int {
        val ranges = line.split(",")
        val range1 = ranges[0].split("-").let { it[0].toInt()..it[1].toInt() }
        val range2 = ranges[1].split("-").let { it[0].toInt()..it[1].toInt() }
        val range1inRange2 = range1.first in range2 && range1.last in range2
        val range2inRange1 = range2.first in range1 && range2.last in range1
        return if (range1inRange2 || range2inRange1) 1 else 0
    }

    fun part1(input: List<String>): Int {
        val totalRanges = input.sumOf { line ->
            containedRangesInPair(line.trim())
        }
        return totalRanges
    }

    fun overlappingRangesInPair(line: String): Int {
        val ranges = line.split(",")
        val range1 = ranges[0].split("-").let { it[0].toInt()..it[1].toInt() }
        val range2 = ranges[1].split("-").let { it[0].toInt()..it[1].toInt() }
        val range1inRange2 = range1.first in range2 || range1.last in range2
        val range2inRange1 = range2.first in range1 || range2.last in range1
        return if (range1inRange2 || range2inRange1) 1 else 0
    }

    fun part2(input: List<String>): Int {
        val totalRanges = input.sumOf { line ->
            overlappingRangesInPair(line.trim())
        }
        return totalRanges
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    println("test result contained pairs total: ${part1(testInput)}")
    check(part1(testInput) == 2)
    println("test result overlapping pairs total is: ${part2(testInput)}")
    check(part2(testInput) == 4)

    val input = readInput("Day04_input")
    println("Contained pairs total is: ${part1(input)}")
    println("Overlapping pairs total is: ${part2(input)}")
}
