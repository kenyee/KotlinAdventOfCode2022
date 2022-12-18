data class Sensor(
    val x: Int,
    val y: Int,
    val beaconX: Int,
    val beaconY: Int
) {
    internal val dX = Math.abs(x - beaconX)
    internal val dY = Math.abs(y - beaconY)

    fun isInRange(posX: Int, posY: Int): Boolean {
        return posX in (x - dX)..(x + dX) &&
            posY in (y - dY)..(y + dY)
    }

    fun isInYRange(posY: Int): Boolean {
        return posY in (y - dY)..(y + dY)
    }

    fun getXRangeForRow(posY: Int): IntRange {
        val rowDiff = Math.abs(y - posY)
        val colDiff = Math.abs(dX + dY - rowDiff)
        return (x - colDiff)..(x + colDiff)
    }
}

fun main() { // ktlint-disable filename
    val regex = "Sensor at x=([-0-9]+), y=([-0-9]+): closest beacon is at x=([-0-9]+), y=([-0-9]+)".toRegex()

    fun part1(input: List<String>): Int {
        // find grid size
        val sensors = mutableListOf<Sensor>()
        var minX = Integer.MAX_VALUE
        var maxX = Integer.MIN_VALUE
        var minY = Integer.MAX_VALUE
        var maxY = Integer.MIN_VALUE
        for (line in input) {
            val matches = regex.find(line)
            val (sX, sY, bX, bY) = matches!!.groupValues.drop(1).map { it.toInt() }
            val sensor = Sensor(sX, sY, bX, bY)
            sensors.add(sensor)
            minX = Math.min(minX, sX - sensor.dX)
            maxX = Math.max(maxX, sX + sensor.dX)
            minY = Math.min(minY, sY - sensor.dY)
            maxY = Math.max(maxY, sY + sensor.dY)
        }
        val numRows = maxY - minY
        val numCols = maxX - minX
        val scanRow = 10
        println("Grid size: ($minX,$minY)  ($maxX,$maxY)  numRows:$numRows, numCols:$numCols, numSensors:${sensors.size}")
        val sensorsInRange = sensors.filter { it.isInYRange(scanRow) }
        println("filtered Sensors: $sensorsInRange")
        val row = CharArray(numCols+1) { '.' }
        for (sensor in sensorsInRange) {
            val xRange = sensor.getXRangeForRow(scanRow)
            println("row $scanRow range for $sensor is $xRange")
            for (x in xRange) {
                // normalize x pos to 0
                val normalizedX = x - minX
                if (normalizedX in row.indices) {
                    row[normalizedX] = '#'
                }
            }
        }
        for (sensor in sensorsInRange) {
            if (sensor.beaconY == scanRow) {
                row[sensor.beaconX - minX] = 'B'
            }
        }
        val invalid = row.count { it == '#' }
        println("Row 10 is: $minX ${String(row)} $maxX")

        // for each scanned beacon, get dx, dy.  Fill diamond area with # corners are dx+dy
        return invalid
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    println("Test #no sensor positions in row 10: ${part1(testInput)}")
    check(part1(testInput) == 26)
//    println("Test #Sand units with floor: ${part2(testInput)}")
//    check(part2(testInput) == 93)

//    val input = readInput("Day15_input")
//    println("#Sand units: ${part1(input)}")
//    println("#Sand units with floor: ${part2(input)}")
}
