

fun main() { // ktlint-disable filename
    fun getTopCalories(input: List<String>): List<Int> {
        var elfCal = 0
        val calorieSubtotals = mutableListOf<Int>()

        for (line in input) {
            if (line.isEmpty()) {
                calorieSubtotals.add(elfCal)
                elfCal = 0
            } else {
                elfCal += line.toInt()
            }
        }
        // check last elf
        if (elfCal > 0) {
            calorieSubtotals.add(elfCal)
        }
        return calorieSubtotals.sortedDescending()
    }

    fun part1(input: List<String>): Int {
        val topCalories = getTopCalories(input)
        return topCalories.first()
    }

    fun part2(input: List<String>): Int {
        val topCalories = getTopCalories(input)
        return topCalories.take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    println("test result most calories is: ${part1(testInput)}")
    check(part1(testInput) == 24000)
    println("test result 3 max calories total is: ${part2(testInput)}")
    check(part2(testInput) == 45000)

    val input = readInput("Day01_input")
    println("Input data max calories is: ${part1(input)}")
    println("Input data 3 max calories total is: ${part2(input)}")
}
