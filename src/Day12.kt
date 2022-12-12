
fun main() { // ktlint-disable filename
    val paths = mutableSetOf<String>()

    fun isValidStep(
        visited: Array<BooleanArray>,
        grid: List<String>,
        row: Int,
        col: Int,
        curHeight: Char
    ): Boolean {
        val numRows = grid.size
        val numCols = grid[0].length
        if (row < 0 || row >= numRows) return false
        if (col < 0 || col >= numCols) return false
        if (visited[row][col]) return false
        val checkHeight = grid[row][col]
        if (curHeight == 'a' || curHeight == 'b') {
            if (checkHeight == 'S' || checkHeight == 'a') return true
            return false
        }
        if (curHeight == 'E') {
            if (checkHeight == 'z' || checkHeight == 'y') return true
            return false
        }
        if (curHeight > checkHeight && curHeight - 1 != checkHeight) return false
        return true
    }

    fun searchStart(
        visited: Array<BooleanArray>,
        grid: List<String>,
        startRow: Int,
        startCol: Int,
        endChar: Char = 'S'
    ): String {
        val queue = ArrayDeque<Pair<Position, String>>()

        queue.add(Pair(Position(startCol, startRow), ""))
        visited[startRow][startCol] = true
        while (queue.isNotEmpty()) {
            val (curPos, curPath) = queue.removeFirst()
            val thisHeight = grid[curPos.y][curPos.x]
            val path = curPath + thisHeight

            if (thisHeight == endChar) {
                paths.add(path)
                println("Found path $path")
                return path
            }
            for (direction in Direction.values()) {
                if (isValidStep(visited, grid, curPos.y + direction.deltaY, curPos.x + direction.deltaX, thisHeight)) {
                    queue.add(Pair(Position(curPos.x + direction.deltaX, curPos.y + direction.deltaY), path))
                    visited[curPos.y + direction.deltaY][curPos.x + direction.deltaX] = true
                }
            }
        }
        return ""
    }

    fun processInput(input: List<String>, endChar: Char = 'S'): Int {
        val numCols = input[0].length
        val visited = Array(input.size) {
            BooleanArray(numCols) { false }
        }
        paths.clear()
        var result = ""
        for (i in input.indices) {
            val line = input[i]
            val startIndex = line.indexOf('E')
            if (startIndex > -1) {
                result = searchStart(visited, input, i, startIndex, endChar)
                break
            }
        }

        return result.length - 1 /* start is not a step but is recorded */
    }

    fun part1(input: List<String>): Int {
        return processInput(input)
    }

    fun part2(input: List<String>): Int {
        return processInput(input, 'a')
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    println("Test Fewest steps: ${part1(testInput)}")
    check(part1(testInput) == 31)
    println("Test Fewest steps to bottom: ${part2(testInput)}")
    check(part2(testInput) == 29)

    val input = readInput("Day12_input")
    println("Fewest steps: ${part1(input)}")
    println("Fewest steps to bottom: ${part2(input)}")
}
