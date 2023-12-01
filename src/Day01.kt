fun main() {
    fun String.toCalibration(): Int {
        val left = """(\d).*""".toRegex()
        val right = """.*(\d)""".toRegex()
        return (left.find(this)!!.groupValues.last() + right.find(this)!!.groupValues.last()).toInt()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { it.toCalibration() }
    }

    fun part2(input: List<String>): Int {
        val textToNumLeft = """(one|two|three|four|five|six|seven|eight|nine).*""".toRegex()
        val textToNumRight = """.*(one|two|three|four|five|six|seven|eight|nine)""".toRegex()

        fun textToNum(m: String) =
            when (m) {
                "one" -> "1"
                "two" -> "2"
                "three" -> "3"
                "four" -> "4"
                "five" -> "5"
                "six" -> "6"
                "seven" -> "7"
                "eight" -> "8"
                "nine" -> "9"
                else -> {
                    throw Exception("Unknown number")
                }
            }

        return input.sumOf {
            val convertedTextLeft = textToNumLeft.replace(it) {
                textToNum(it.groupValues.last())
            }

            val convertedTextRight = textToNumRight.replace(it) {
                textToNum(it.groupValues.last())
            }
            (convertedTextLeft + convertedTextRight).toCalibration()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    // test if implementation meets criteria from the description, like:
    val testInput2 = readInput("Day01_part2_test")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
