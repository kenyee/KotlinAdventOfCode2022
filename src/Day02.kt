

fun main() { // ktlint-disable filename
    val winningRoundValue = 6
    val tieRoundValue = 3
    val elfShapeValue = mapOf(
        'A' to 1, // rock
        'B' to 2, // paper
        'C' to 3 // scissors
    )
    val myShapeValue = mapOf(
        'X' to 1,
        'Y' to 2,
        'Z' to 3
    )
    val winValue = mapOf( // gets object that wins from specified object
        3 to 1,
        1 to 2,
        2 to 3
    )
    val loseValue = mapOf( // gets object that wins from specified object
        1 to 3,
        2 to 1,
        3 to 2
    )

    fun winScore(elfShape: Char, myShape: Char): Int {
        val elfObject = elfShapeValue[elfShape]!!
        val myObject = myShapeValue[myShape]!!
        if (elfObject == myObject) return myObject + tieRoundValue
        val myWin = (myObject == 1 && elfObject == 3) ||
            (myObject == 2 && elfObject == 1) ||
            (myObject == 3 && elfObject == 2)
        return if (myWin) {
            myObject + winningRoundValue
        } else myObject
    }

    fun part1(input: List<String>): Int {
        val score = input.sumOf { line ->
            winScore(line[0], line[2])
        }
        return score
    }

    fun windrawloseScore(elfShape: Char, myShape: Char): Int {
        val elfObject = elfShapeValue[elfShape]!!
        return when (myShape) {
            'X' -> { // lose
                loseValue[elfObject]!! + 0
            }
            'Y' -> { // tie
                elfObject + tieRoundValue
            }
            'Z' -> { // win
                winValue[elfObject]!! + winningRoundValue
            }
            else -> 0
        }
    }

    fun part2(input: List<String>): Int {
        val score = input.sumOf { line ->
            windrawloseScore(line[0], line[2])
        }
        return score
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    println("test result score: ${part1(testInput)}")
    check(part1(testInput) == 15)
    println("test result draw/win/lose score is: ${part2(testInput)}")
    check(part2(testInput) == 12)

    val input = readInput("Day02_input")
    println("Score is: ${part1(input)}")
    println("draw/win/lose score is: ${part2(input)}")
}
