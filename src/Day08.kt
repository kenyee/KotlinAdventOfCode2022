import java.util.concurrent.atomic.AtomicInteger

enum class Direction(val deltaX: Int, val deltaY: Int) {
    Up(0, -1),
    Down(0, 1),
    Right(1, 0),
    Left(-1, 0)
}

data class Tree(val row: Int, val col: Int)

fun main() { // ktlint-disable filename
    fun sumInnerTreesInDirection(
        visible: Array<Array<Boolean>>,
        array: List<String>,
        dir: Direction,
        row: Int,
        col: Int
    ): Int {
        val numRows = array.size
        val numCols = array[0].length
        var curRow = row
        var curCol = col
        val lowest = -1
        var tallest = lowest
        var numVisible = 0

        while (curRow >= 0 && curRow <= (numRows - 1) &&
            curCol >= 0 && curCol <= (numCols - 1)
        ) {
            val treeHeight = array[curRow][curCol].digitToIntOrNull()!!
            if (treeHeight > tallest) {
                tallest = treeHeight
                if (!visible[curRow][curCol]) {
                    visible[curRow][curCol] = true
                    numVisible++
                }
            }
            curRow += dir.deltaY
            curCol += dir.deltaX
        }
        return numVisible
    }

    fun part1BruteForce(input: List<String>): Long {
        val grid = input.map { line -> line.map { it.digitToInt() } }
        val colSize = grid[0].size
        val rowSize = grid.size
        val visible = Array(rowSize) {
            Array(colSize) { false }
        }
        // left
        for (i in 0 until rowSize) {
            var height = -1
            for (j in 0 until colSize) {
                if (grid[i][j] > height) {
                    visible[i][j] = true
                    height = grid[i][j]
                }
            }
        }
        // right
        for (i in 0 until rowSize) {
            var height = -1
            for (j in colSize - 1 downTo 0) {
                if (grid[i][j] > height) {
                    visible[i][j] = true
                    height = grid[i][j]
                }
            }
        }
        // top
        for (j in 0 until colSize) {
            var height = -1
            for (i in 0 until rowSize) {
                if (grid[i][j] > height) {
                    visible[i][j] = true
                    height = grid[i][j]
                }
            }
        }
        // bottom
        for (j in 0 until colSize) {
            var height = -1
            for (i in rowSize - 1 downTo 0) {
                if (grid[i][j] > height) {
                    visible[i][j] = true
                    height = grid[i][j]
                }
            }
        }
        return visible.sumOf { row -> row.count { it } }.toLong()
    }

    fun part1InsideVisible(grid: List<String>): Long {
        val colSize = grid[0].length
        val rowSize = grid.size
        val visible = Array(rowSize) {
            Array(colSize) { false }
        }

        var numVisible = 0L
        for (j in 0 until colSize) {
            val column = (0 until rowSize).map { grid[it][j] }
            for (i in 0 until rowSize) {
                val curValue = grid[j][i]
                val left = grid[i].substring(0, j)
                val isLeftVisible = left.isNotEmpty() && left.all { it < curValue }
                val right = grid[i].substring(j + 1, colSize)
                val isRightVisible = right.isNotEmpty() && right.all { it < curValue }
                val top = column.subList(0, i)
                val isTopVisible = top.isNotEmpty() && top.all { it < curValue }
                val bottom = column.subList(i + 1, rowSize)
                val isBottomVisible = bottom.isNotEmpty() && bottom.all { it < curValue }
                val isEdge = i == 0 || j == 0 || i == rowSize - 1 || j == colSize - 1
                val isVisibleInAnyDirection =
                    isLeftVisible || isRightVisible || isTopVisible || isBottomVisible
                if (!visible[i][j] && (isVisibleInAnyDirection || isEdge)) {
                    visible[i][j] = true
                    numVisible++
                }
            }
        }
        return numVisible
    }

    fun part1(grid: List<String>): Long {
        val colSize = grid[0].length
        val rowSize = grid.size
        val visible = Array(rowSize) {
            Array(colSize) { false }
        }
        var treesVisible = 0L
        for (i in 1 until colSize - 1) {
            treesVisible += sumInnerTreesInDirection(visible, grid, Direction.Down, 0, i) // top row
        }
        for (i in 1 until colSize - 1) {
            treesVisible += sumInnerTreesInDirection(visible, grid, Direction.Up, rowSize - 1, i) // bottom row
        }
        for (i in 1 until rowSize - 1) {
            treesVisible += sumInnerTreesInDirection(visible, grid, Direction.Right, i, 0) // left column
        }
        for (i in 1 until rowSize - 1) {
            treesVisible += sumInnerTreesInDirection(visible, grid, Direction.Left, i, colSize - 1) // right column
        }
        val numCorners = 4
        return treesVisible + numCorners
    }

    fun part1SomeoneAtomicInt(input: List<String>): Int {
        val array = input.map { it.chars().toArray() }.toTypedArray()

        val visibility = Array(array.size) {
            Array(array[0].size) { false }
        }

        var count = 0

        fun AtomicInteger.updateVisibility(i: Int, j: Int) {
            if (array[i][j] > this.get()) {
                if (!visibility[i][j]) {
                    count++
                    visibility[i][j] = true
                }
                this.set(array[i][j])
            }
        }

        for (i in array.indices) {
            AtomicInteger(-1).apply { array.indices.forEach { updateVisibility(i, it) } }
            AtomicInteger(-1).apply { array.indices.reversed().forEach { updateVisibility(i, it) } }
            AtomicInteger(-1).apply { array.indices.forEach { updateVisibility(it, i) } }
            AtomicInteger(-1).apply { array.indices.reversed().forEach { updateVisibility(it, i) } }
        }

        return count
    }

    fun treesUntilBlocked(
        array: List<String>,
        dir: Direction,
        row: Int,
        col: Int
    ): Int {
        val numRows = array.size
        val numCols = array[0].length
        var curRow = row
        var curCol = col
        var numVisible = 0

        val positionHeight = array[curRow][curCol]
        curRow += dir.deltaY
        curCol += dir.deltaX
        while (curRow >= 0 && curRow <= (numRows - 1) &&
            curCol >= 0 && curCol <= (numCols - 1)
        ) {
            val treeHeight = array[curRow][curCol]
            numVisible++
            if (treeHeight >= positionHeight) {
                break
            }
            curRow += dir.deltaY
            curCol += dir.deltaX
        }
        return numVisible
    }

    fun part2(grid: List<String>): Int {
        val colSize = grid[0].length
        val rowSize = grid.size
        var maxCount = 0
        for (i in 0 until rowSize) {
            for (j in 0 until colSize) {
                val sum =
                    treesUntilBlocked(grid, Direction.Left, i, j) *
                        treesUntilBlocked(grid, Direction.Right, i, j) *
                        treesUntilBlocked(grid, Direction.Up, i, j) *
                        treesUntilBlocked(grid, Direction.Down, i, j)
                maxCount = Math.max(maxCount, sum)
            }
        }
        return maxCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    println("Test total# of visible trees: ${part1(testInput)}")
    check(part1(testInput) == 21L)
    println("Test most scenic score: ${part2(testInput)}")
    check(part2(testInput) == 8)

    val input = readInput("Day08_input")
    println("Total# of visible trees: ${part1(input)}")
    println("Most scenic score is: ${part2(input)}")
}
