
fun main() { // ktlint-disable filename

    fun calcTopCrates(input: List<String>, multiMove: Boolean = false): String {
        var stackCountIndex = 0
        var stackCount = 0
        for (i in input.indices) {
            if (input[i].startsWith(" 1 ")) {
                stackCount = input[i].split("").filter { it.isNotBlank() }.size
                stackCountIndex = i
                break
            }
        }
        // crate positions are always at 1, 5, 9, etc.
        val stacks = Array<ArrayDeque<Char>>(stackCount) { ArrayDeque() }
        for (i in stackCountIndex downTo 0) {
            for (j in 1 until input[i].length step 4) {
                val crateId = input[i][j]
                if (crateId != ' ') {
                    val stackNum = (j - 1) / 4
                    stacks[stackNum].add(crateId)
                }
            }
        }

        for (i in stackCountIndex + 2 until input.size) {
            // move 1 from 2 to 1
            val tokens = input[i].split(" ")
            val count = tokens[1].toInt()
            val from = tokens[3].toInt() - 1
            val to = tokens[5].toInt() - 1
            val crates = stacks[from].takeLast(count) // gets in-order list of last N crates
            for (i in 0 until count) {
                stacks[from].removeLast()
            }
            stacks[to].addAll(if (multiMove) crates else crates.reversed())
        }

        val result = StringBuilder()
        for (stack in stacks) {
            if (stack.isNotEmpty()) {
                result.append(stack.last())
            }
        }

        return result.toString()
    }

    fun part1(input: List<String>): String {
        return calcTopCrates(input)
    }

    fun part2(input: List<String>): String {
        return calcTopCrates(input, multiMove = true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    println("test result top crates: ${part1(testInput)}")
    check(part1(testInput) == "CMZ")
    println("test result multi-move top crates is: ${part2(testInput)}")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05_input")
    println("Top crates is: ${part1(input)}")
    println("Multi-move top crates is: ${part2(input)}")
}
