
fun main() { // ktlint-disable filename
    val packetMarkerSize = 4
    val msgMarkerSize = 14

    fun findMarker(encodedMsg: String, markerSize: Int): Int {
        encodedMsg.windowed(markerSize).forEachIndexed { index, window ->
            if (window.toCharArray().distinct().size == markerSize) {
                return index + markerSize
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        return findMarker(input[0], packetMarkerSize)
    }

    fun part2(input: List<String>): Int {
        return findMarker(input[0], msgMarkerSize)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    println("Test packet marker #chars: ${part1(testInput)}")
    check(part1(testInput) == 7)
    println("Test msg marker #chars: ${part2(testInput)}")
    check(part2(testInput) == 19)

    val input = readInput("Day06_input")
    println("Packet Marker #chars is: ${part1(input)}")
    println("Msg Marker #chars  is: ${part2(input)}")
}
