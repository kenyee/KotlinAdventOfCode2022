fun Array<Int>.indexPos(signedPos: Int): Int {
    return if (signedPos < 0) (this.size - 1 + (signedPos).rem(this.lastIndex)) else signedPos.rem(this.size)
}

fun main() { // ktlint-disable filename
    fun part1(input: List<String>): Int {
        val moves = input.map { it.toInt() }
        val numbers = moves.toTypedArray()
        moves.filter { it != 0 }.forEach { i ->
            val oldPos = numbers.indexOf(i)
            val newPos = numbers.indexPos(oldPos + i)
            val destIndex = minOf(oldPos, newPos + 1)
            val startIndex = minOf(oldPos + 1, newPos)
            val endIndex = maxOf(oldPos, newPos + 1)
            // println("copyright i:$i dest:$destIndex start:$startIndex end:$endIndex")
            // shift array so there's space at the destination
            numbers.copyInto(numbers, destIndex, startIndex, endIndex)
            numbers[newPos] = i
            println("numbers are: ${numbers.toList()}")
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    println("Grove coordinates sum: ${part1(testInput)}")
    check(part1(testInput) == 64)
//    println("Test #Exterior sides: ${part2(testInput)}")
//    check(part2(testInput) == 58)

//    val input = readInput("Day20_input")
//    println("#Unconnected sides: ${part1(input)}")
//    println("#Exterior sides: ${part2(input)}")
}
