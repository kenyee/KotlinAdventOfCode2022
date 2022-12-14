import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray

fun main() { // ktlint-disable filename
    fun parseIntoQueue(line: String): JsonArray {
        val jsonArray = Json.decodeFromString(JsonArray.serializer(), line)
        return jsonArray
    }

    fun isInOrder(left: JsonArray, right: JsonArray): Boolean {
        println("comparing $left and $right")
        if (left.size > right.size) return false
        var equalElements = true
        for (i in left.indices) {
            when {
                i >= right.size -> {
                    return !equalElements
                }
                left[i] is JsonArray && right[i] is JsonArray -> {
                    if (!isInOrder(left[i].jsonArray, right[i].jsonArray)) return false
                }
                left[i] !is JsonArray && right[i] !is JsonArray -> {
                    if (left[i].toString().toInt() > right[i].toString().toInt()) return false
                    // handle weird case where at least one element must be non-equal
                    if (left[i].toString().toInt() < right[i].toString().toInt()) equalElements = false
                }
                left[i] !is JsonArray && right[i] is JsonArray -> {
                    if (right[i].jsonArray.isEmpty()) return false
                    if (right[i].jsonArray[0] is JsonArray) return false
                    if (left[i].toString().toInt() >
                        right[i].jsonArray[0].toString().toInt()
                    ) return false
                }
                left[i] is JsonArray && right[i] !is JsonArray -> {
                    if (left[i].jsonArray.isEmpty()) return false
                    if (left[i].jsonArray[0] is JsonArray) return false
                    if (left[i].jsonArray[0].toString().toInt() > right[i].toString()
                        .toInt()
                    ) return false
                    // handle weird case where at least one element must be non-equal
                    if (left[i].jsonArray[0].toString().toInt() < right[i].toString().toInt()) equalElements = false
                }
            }
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val validIndexes = mutableListOf<Int>()
        for (i in input.indices step 3) {
            val left = parseIntoQueue(input[i])
            val right = parseIntoQueue(input[i + 1])
            if (isInOrder(left, right)) validIndexes.add(i / 3 + 1)
        }
        println("Valid indexes: $validIndexes")
        return validIndexes.sum()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    println("Test Sum of matching pair indices: ${part1(testInput)}")
    check(part1(testInput) == 13)
//    println("Test Fewest steps to bottom: ${part2(testInput)}")
//    check(part2(testInput) == 29)

    val input = readInput("Day13_input")
    println("Sum of matching pair indices: ${part1(input)}")
//    println("Fewest steps to bottom: ${part2(input)}")
}
