import kotlin.math.pow

fun main() {
    fun String.toMatches(): Int {
        val (before, after) = this.substringAfter(":").split("|")

        val winningNumbers = before.trim().split("\\s+".toRegex()).map { it.toInt() }.toSet()
        val myNumbers = after.trim().split("\\s+".toRegex()).map { it.toInt() }.toSet()
        return winningNumbers.intersect(myNumbers).size
    }

    fun String.toPoints(): Int {
        val matches = this.toMatches()
        return if (matches == 0) {
            0
        } else {
            2.0.pow(matches - 1).toInt()
        }
    }
    fun part1(input: List<String>): Int {
        return input.sumOf {
            it.toPoints()
        }
    }

    fun part2(input: List<String>): Int {
        val cardToCopies = mutableMapOf<Int, Int>().withDefault { 0 }
        input.forEachIndexed { index, row ->
            // Initialize
            cardToCopies[index] = cardToCopies.getValue(index) + 1
            val matches = row.toMatches()
            for (i in 1..matches) {
                cardToCopies[index + i] = cardToCopies.getValue(index + i) + cardToCopies.getValue(index)
            }
        }
        return cardToCopies.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    // test if implementation meets criteria from the description, like:
    val testInput2 = readInput("Day04_test")
    check(part2(testInput2) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
