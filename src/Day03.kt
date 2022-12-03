
fun main() { // ktlint-disable filename

    fun getPriorityScore(priority: Char) = if (priority.isUpperCase()) {
        priority - 'A' + 27
    } else {
        priority - 'a' + 1
    }

    fun calcPriority(line: String): Int {
        val rucksacks = line.chunked(line.length / 2)
        val priority = rucksacks[0].toCharArray().intersect(rucksacks[1].toSet()).first()
        return getPriorityScore(priority)
    }

    fun part1(input: List<String>): Int {
        val totalPriority = input.sumOf { line ->
            calcPriority(line.trim())
        }
        return totalPriority
    }

    fun calcBadges(input: List<String>): Int {
        return input.chunked(3).sumOf { rucksacks ->
            getPriorityScore(
                rucksacks[0].toCharArray()
                    .intersect(rucksacks[1].toSet())
                    .intersect(rucksacks[2].toSet()).first()
            )
        }
    }

    fun part2(input: List<String>): Int {
        return calcBadges(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    println("test result priority total: ${part1(testInput)}")
    check(part1(testInput) == 157)
    println("test result badge priority total is: ${part2(testInput)}")
    check(part2(testInput) == 70)

    val input = readInput("Day03_input")
    println("Priority total is: ${part1(input)}")
    println("Badge priority total is: ${part2(input)}")
}
