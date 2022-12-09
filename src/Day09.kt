
import kotlin.math.sign

fun main() { // ktlint-disable filename
    val knots = mutableListOf<Position>()
    val visited = mutableSetOf<Position>()

    fun moveRope(dir: Direction, distance: Int) {
        val tailIndex = knots.size - 1
        val indexPairs = knots.indices.windowed(size = 2, step = 1)
        repeat(distance) {
            knots[0] = knots[0] + dir
            for ((head, tail) in indexPairs) {
                if (!knots[tail].isAdjacent(knots[head])) {
                    knots[tail] = Position(
                        knots[tail].x + (knots[head].x - knots[tail].x).sign,
                        knots[tail].y + (knots[head].y - knots[tail].y).sign
                    )
                }
                if (tail == tailIndex) {
                    visited.add(knots[tail])
                }
            }
        }
    }

    fun parseCommands(input: List<String>) {
        for (line in input) {
            val tokens = line.split(" ")
            val cmd = tokens[0]
            val distance = tokens[1].toInt()
            when (cmd) {
                "R" -> moveRope(Direction.Right, distance)
                "L" -> moveRope(Direction.Left, distance)
                "U" -> moveRope(Direction.Up, distance)
                "D" -> moveRope(Direction.Down, distance)
            }
        }
    }

    fun reset() {
        visited.clear()
        visited.add(Position(0, 0))
        knots.clear()
    }

    fun part1(input: List<String>): Int {
        reset()
        repeat(2) { knots.add(Position(0, 0)) }

        parseCommands(input)

        return visited.size
    }

    fun part2(input: List<String>): Int {
        reset()
        repeat(10) { knots.add(Position(0, 0)) }

        parseCommands(input)

        return visited.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    println("Test total# tail positions: ${part1(testInput)}")
    check(part1(testInput) == 13)
    println("Test total# long tail positions: ${part2(testInput)}")
    check(part2(testInput) == 1)

    val input = readInput("Day09_input")
    println("Total# of tail positions: ${part1(input)}")
    println("Total# of long tail positions: ${part2(input)}")
}
