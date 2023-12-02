import java.lang.Integer.max

fun main() {
    val bag = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14,
    )

    fun part1(input: List<String>): Int {
        val regex = """Game (\d+):(.*)""".toRegex()
        val gameColours = """(\d+) (red|green|blue)""".toRegex()
        return input.sumOf {
            val (gameId, game) = regex.matchEntire(it)!!.destructured
            val bagGrab = game.split("; ")

            if (bagGrab.all {
                    it.split(", ").all {
                        val (count, colour) = gameColours.matchEntire(it.trim())!!.destructured
                        bag[colour]!! >= count.toInt()
                    }
                }
            ) {
                gameId.toInt()
            } else {
                0
            }
        }
    }

    fun part2(input: List<String>): Int {
        val regex = """Game (\d+):(.*)""".toRegex()
        val gameColours = """(\d+) (red|green|blue)""".toRegex()
        return input.sumOf {
            val (_, game) = regex.matchEntire(it)!!.destructured
            val bagGrab = game.split("; ")
            val maxBag = mutableMapOf(
                "red" to 0,
                "green" to 0,
                "blue" to 0,
            )

            bagGrab.forEach {
                it.split(", ").forEach {
                    val (count, colour) = gameColours.matchEntire(it.trim())!!.destructured
                    maxBag[colour] = max(maxBag[colour]!!, count.toInt())
                }
            }
            val total = maxBag.values.fold(1) { acc, i -> acc * i }
            total
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    // test if implementation meets criteria from the description, like:
    val testInput2 = readInput("Day02_part2_test")
    check(part2(testInput2) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
