data class Valve(
    val name: String,
    val rate: Int,
    val connections: List<String>
)
val valveLookup = mutableMapOf<String, Valve>()

fun main() { // ktlint-disable filename
    val regex = "Valve ([A-Z]+) has flow rate=([0-9]+); tunnel[s]* lead[s]* to valve[s]* ([A-Z, ]+)".toRegex()

    fun searchMax(
        valve: Valve,
        visited: MutableList<String>,
        currentList: MutableMap<String, Int>,
        minutesLeft: Int
    ): Int {
        if (minutesLeft == 0) return 0
        if (visited.contains(valve.name)) return 0
        visited.add(valve.name)
        val maxChild = valve.connections.map {
            it to searchMax(valveLookup[it]!!, visited, currentList, minutesLeft - 1)
        }.maxByOrNull { it.second }
        visited.remove(valve.name)
        val maxHere = valve.rate * minutesLeft + (maxChild?.second ?: 0)
        currentList[valve.name] = maxHere
        return maxHere
    }

    fun part1(input: List<String>): Int {
        // generate graph
        for (line in input) {
            val matches = regex.find(line)
                ?: throw IllegalStateException("Couldn't parse $line")
            val valve = matches.groupValues[1]
            val rate = matches.groupValues[2].toInt()
            val tunnelsTo = matches.groupValues[3].split(", ")
            valveLookup[valve] = Valve(valve, rate, tunnelsTo)
        }
        // get max rate shortest path through graph
        val visited = mutableListOf<String>()
        val valveList = mutableMapOf<String, Int>()
        val max = searchMax(valveLookup["AA"]!!, visited, valveList, minutesLeft = 30)
        println("max $max for valve list is $valveList ")

        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    println("Test max pressure in 30min: ${part1(testInput)}")
    check(part1(testInput) == 1651)
//    println("Test #Sand units with floor: ${part2(testInput)}")
//    check(part2(testInput) == 93)

//    val input = readInput("Day16_input")
//    println("#no sensor positions in row 20000: ${part1(input)}")
//    println("#Sand units with floor: ${part2(input)}")
}
