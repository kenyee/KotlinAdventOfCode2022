data class Point3D(
    val x: Int,
    val y: Int,
    val z: Int
) {
    val neighbors: Set<Point3D> by lazy {
        setOf(
            this.copy(x = x - 1),
            this.copy(x = x + 1),
            this.copy(y = y - 1),
            this.copy(y = y + 1),
            this.copy(z = z - 1),
            this.copy(z = z + 1)
        )
    }
}

data class BoundingBox(
    val xRange: IntRange,
    val yRange: IntRange,
    val zRange: IntRange
) {
    fun contains(point: Point3D) =
        point.x in xRange && point.y in yRange && point.z in zRange
}

fun Set<Point3D>.getAirBoundingBox(): BoundingBox {
    var minX = Integer.MAX_VALUE
    var maxX = Integer.MIN_VALUE
    var minY = Integer.MAX_VALUE
    var maxY = Integer.MIN_VALUE
    var minZ = Integer.MAX_VALUE
    var maxZ = Integer.MIN_VALUE
    this.forEach {
        if (it.x < minX) minX = it.x
        if (it.x > maxX) maxX = it.x
        if (it.y < minY) minY = it.y
        if (it.y > maxY) maxY = it.y
        if (it.z < minZ) minZ = it.z
        if (it.z > maxZ) maxZ = it.z
    }
    return BoundingBox(
        minX - 1..maxX + 1,
        minY - 1..minY + 1,
        minZ - 1..maxZ + 1
    )
}

fun main() { // ktlint-disable filename
    fun part1(input: List<String>): Int {
        val points = mutableSetOf<Point3D>()
        for (line in input) {
            val (x, y, z) = line.split(",").map { it.toInt() }
            points.add(Point3D(x, y, z))
        }
        // unconnected faces are all neighbors not in the point set
        return points.flatMap { it.neighbors }.count { it !in points }
    }

    fun part2(input: List<String>): Int {
        val points = mutableSetOf<Point3D>()
        for (line in input) {
            val (x, y, z) = line.split(",").map { it.toInt() }
            points.add(Point3D(x, y, z))
        }
        val boundingBox = points.getAirBoundingBox()
        val queue = ArrayDeque<Point3D>()
        val firstSearchPoint = Point3D(boundingBox.xRange.first, boundingBox.yRange.first, boundingBox.zRange.first)
        queue.add(firstSearchPoint)
        val visited = mutableSetOf<Point3D>()
        val exterior = mutableSetOf<Point3D>()
        while (queue.isNotEmpty()) {
            val point = queue.removeLast()
            visited.add(point)
            point.neighbors
                .filter { boundingBox.contains(it) && it !in visited }
                .forEach {
                    if (it in points) {
                        exterior.add(it)
                    } else {
                        queue.add(it)
                    }
                }
        }
        return exterior.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    println("Test #Unconnected sides: ${part1(testInput)}")
    check(part1(testInput) == 64)
    println("Test #Exterior sides: ${part2(testInput)}")
    check(part2(testInput) == 58)

    val input = readInput("Day18_input")
    println("#Unconnected sides: ${part1(input)}")
//    println("#Exterior sides: ${part2(input)}")
}
