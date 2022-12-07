
fun main() { // ktlint-disable filename
    data class DirInfo(
        val path: String,
        var size: Long = 0,
        val subDirs: MutableList<String> = mutableListOf()
    )

    fun getDirInfo(input: List<String>): Map<String, DirInfo> {
        val dirInfo = mutableMapOf<String, DirInfo>()
        var curDir = "/"
        for (line in input) {
            when {
                line.startsWith("\$ cd ") -> {
                    when (val dir = line.substring("\$ cd ".length)) {
                        "/" -> curDir = dir
                        ".." -> curDir = curDir.substring(0, curDir.lastIndexOf("/"))
                        else -> curDir += if (curDir == "/") dir else "/$dir"
                    }
                }

                line.startsWith("\$ ls") -> {
                    dirInfo[curDir] = DirInfo(curDir)
                }

                line.startsWith("dir ") -> {
                    dirInfo[curDir]!!.subDirs.add(line.substring("dir ".length))
                }

                else -> {
                    // println("file line is $line")
                    // handle file info
                    val tokens = line.split(" ")
                    val fileSize = tokens[0].toLong()
                    // add file size to dir and all parent dirs
                    var dir = curDir
                    while (true) {
                        dirInfo[dir]!!.size += fileSize
                        if (dir == "/") break
                        dir = dir.substring(0, dir.lastIndexOf("/"))
                        if (dir.isEmpty()) dir = "/"
                    }
                }
            }
        }
        return dirInfo
    }

    fun part1(input: List<String>): Long {
        val maxSize = 100000
        val dirInfo = getDirInfo(input)
        val dirsLessThanMax = dirInfo.map { it.value }.filter { it.size < maxSize }
        return dirsLessThanMax.sumOf { it.size }
    }

    fun part2(input: List<String>): Long {
        val maxDisk = 70000000
        val minFree = 30000000
        val dirInfo = getDirInfo(input)
        val rootDir = dirInfo["/"]!!
        val minRemoveSize = minFree - (maxDisk - rootDir.size)
        val candidates = dirInfo.map { it.value }
            .filter { it.size >= minRemoveSize && it.size != rootDir.size }
        println("Possible candidates: $candidates")
        return candidates.map { it.size }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    println("Test total size for dirs under 100000: ${part1(testInput)}")
    check(part1(testInput) == 95437L)
    println("Test smallest directory to delete size: ${part2(testInput)}")
    check(part2(testInput) == 24933642L)

    val input = readInput("Day07_input")
    println("total size for dirs under 100000: ${part1(input)}")
    println("smallest directory to delete size is: ${part2(input)}")
}
