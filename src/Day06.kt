

fun main() {
    fun getWaysToWin(time: Long, recordDistance: Long): Long {
        var ways = 0L
        for (hold in 0..time) {
            val distance = hold * (time - hold)
            if (distance > recordDistance) {
                ways++
            }
        }
        return ways
    }

    fun part1(input: List<String>): Long {
        val times = input.first().split(":").last().trim().split("\\s+".toRegex()).map { it.toLong() }
        val distance = input.last().split(":").last().trim().split("\\s+".toRegex()).map { it.toLong() }
        return times.zip(distance).map {
            getWaysToWin(it.first, it.second)
        }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        val time = input.first().split(":").last().trim().split("\\s+".toRegex()).joinToString("") { it }.toLong()
        val distance = input.last().split(":").last().trim().split("\\s+".toRegex()).joinToString("") { it }.toLong()
        return getWaysToWin(time, distance)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)

    // test if implementation meets criteria from the description, like:
    val testInput2 = readInput("Day06_test")
    check(part2(testInput2) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
