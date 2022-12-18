sealed class Block {
    class HorizLine : Block() {
        override val shape = listOf(
            "####".toCharArray()
        )
    }
    class Cross : Block() {
        override val shape = listOf(
            ".#.".toCharArray(),
            "###".toCharArray(),
            ".#.".toCharArray()
        )
    }
    class Corner : Block() {
        override val shape = listOf(
            "..#".toCharArray(),
            "..#".toCharArray(),
            "###".toCharArray()
        )
    }
    class VertLine : Block() {
        override val shape = listOf(
            "#".toCharArray(),
            "#".toCharArray(),
            "#".toCharArray(),
            "#".toCharArray()
        )
    }
    class Cube : Block() {
        override val shape = listOf(
            "##".toCharArray(),
            "##".toCharArray()
        )
    }

    abstract val shape: List<CharArray>

    fun hitCheck(grid: Array<CharArray>, position: Position): Boolean {
        if (position.x + width >= grid[0].size || position.x == -1) return true
        if (position.y - height == -1) return true
        for (i in shape.indices) {
            for (j in 0 until shape[0].size) {
                if ((shape[shape.size - j - 1][i] == '#') &&
                    (grid[position.y - j][i + position.x] == '#')) {
                    return true
                }
            }
        }
        return false
    }

    fun place(grid: Array<CharArray>, position: Position) {
        for (i in shape.indices) {
            for (j in 0 until shape[0].size) {
                if (shape[shape.size - j - 1][i] == '#') {
                    grid[position.y - j][i + position.x] = shape[shape.size - j - 1][i]
                }
            }
        }
    }

    private val width: Int
        get() = shape[0].size
    val height: Int
        get() = shape.size
}

fun main() { // ktlint-disable filename
    fun part1(input: List<String>): Int {
        val moves = input[0]
        // grid coordinates go up, zero is at bottom dropping goes down
        var yOffset = 0
        val height = 50 // when height reached, add to offset
        val grid = Array(10000) {
            CharArray(7) { '.' }
        }

        // start pos is 2, y + height + 3 from bottom or highest rock
        var moveNum = 0
        var blockNum = 0
        val blockTypes = listOf(Block.HorizLine(), Block.Cross(), Block.Corner(), Block.VertLine(), Block.Cube())
        var block = blockTypes[blockNum]
        var pos = Position(2, 3 + block.height)
        while (true) {
            if (block.hitCheck(grid, pos)) {
                block.place(grid, pos)
                block = blockTypes[(++blockNum).rem(blockTypes.size)]
                pos = Position(2, 3 + pos.y)
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    println("Test height of rocks: ${part1(testInput)}")
    check(part1(testInput) == 3068)
//    println("Test #Sand units with floor: ${part2(testInput)}")
//    check(part2(testInput) == 93)

//    val input = readInput("Day17_input")
//    println("Height of rocks: ${part1(input)}")
//    println("#Sand units with floor: ${part2(input)}")
}
