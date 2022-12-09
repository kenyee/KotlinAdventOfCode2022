import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

enum class Direction(val deltaX: Int, val deltaY: Int) {
    Up(0, -1),
    Down(0, 1),
    Right(1, 0),
    Left(-1, 0)
}

data class Position(val x: Int, val y: Int)

fun Position.isAdjacent(pos: Position) =
    this.x >= pos.x - 1 && this.x <= pos.x + 1 &&
        this.y >= pos.y - 1 && this.y <= pos.y + 1

operator fun Position.plus(dir: Direction) =
    Position(this.x + dir.deltaX, this.y + dir.deltaY)
