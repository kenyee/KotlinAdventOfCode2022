

fun main() { // ktlint-disable filename

    fun getSandCount(input: List<String>, addFloor: Boolean = false): Int {
        // find grid size
        var numRows = 0
        var numCols = 0
        for (line in input) {
            val coordinates = line.split(" -> ")
            coordinates.windowed(2, 1) {
                val from = it[0].split(",")
                val to = it[1].split(",")
                numRows = Math.max(numRows, from[1].toInt() + 1)
                numRows = Math.max(numRows, to[1].toInt() + 1)
                numCols = Math.max(numCols, from[0].toInt() + 1)
                numCols = Math.max(numCols, to[0].toInt() + 1)
            }
        }
        // println("Grid size: numCols:$numCols, numRows:$numRows")
        if (addFloor) {
            numRows += 2
            numCols = 1000
        }
        val grid = Array(numRows) {
            CharArray(numCols) { '.' }
        }
        // mark obstacles
        if (addFloor) {
            for (i in 0 until numCols) {
                grid[numRows - 1][i] = '#'
            }
        }
        for (line in input) {
            val coordinates = line.split(" -> ")
            coordinates.windowed(2, 1) {
                val from = it[0].split(",").map { num -> num.toInt() }
                val to = it[1].split(",").map { num -> num.toInt() }
                val rowRange = if (from[1] < to[1]) (from[1]..to[1]) else (to[1]..from[1])
                val colRange = if (from[0] < to[0]) (from[0]..to[0]) else (to[0]..from[0])
                for (row in rowRange) {
                    for (col in colRange) {
                        grid[row][col] = '#'
                    }
                }
            }
        }
        // simulate drops
        var sandCount = 0
        var sandRow = 0
        var sandCol = 500
        while (true) {
            try {
                if (grid[sandRow + 1][sandCol] == '.') {
                    // check straight down
                    sandRow++
                } else if (grid[sandRow + 1][sandCol - 1] == '.') {
                    // check down left
                    sandRow++
                    sandCol--
                } else if (grid[sandRow + 1][sandCol + 1] == '.') {
                    // check down right
                    sandRow++
                    sandCol++
                } else {
                    // landed...next sand unit
                    sandCount++
                    // println("Sand $sandCount landed at col:$sandCol row:$sandRow")
                    grid[sandRow][sandCol] = 'O'
                    sandRow = 0
                    sandCol = 500

                    // if entrance blocked (needs to include the one blocking the hole)
                    if (grid[1][499] == 'O' && grid[1][500] == 'O' && grid[1][501] == 'O') return sandCount+1
                }
            } catch (ex: ArrayIndexOutOfBoundsException) {
                // check fell off edge
                return sandCount
            }
        }
    }

    fun part1(input: List<String>): Int {
        return getSandCount(input)
    }

    fun part2(input: List<String>): Int {
        return getSandCount(input, addFloor = true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    println("Test #Sand units: ${part1(testInput)}")
    check(part1(testInput) == 24)
    println("Test #Sand units with floor: ${part2(testInput)}")
    check(part2(testInput) == 93)

    val input = readInput("Day14_input")
    println("#Sand units: ${part1(input)}")
    println("#Sand units with floor: ${part2(input)}")
}
