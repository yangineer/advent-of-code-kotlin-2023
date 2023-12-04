fun main() {
    data class Location(val x: Int, val y: Int)

    fun Char.isPart() = (!this.isDigit() && this != '.')

    fun getNeighbourParts(input: List<String>, y: Int, x: IntRange): Set<Location> {
        // Check top
        val locations = mutableSetOf<Location>()
        if (y > 0) {
            for (i in x) {
                if (input[y - 1][i].isPart()) {
                    locations.add(Location(x = i, y = y - 1))
                }
            }

            // Check top left
            if (x.first > 0 && input[y - 1][x.first - 1].isPart()) {
                locations.add(Location(x = x.first - 1, y = y - 1))
            }

            // Check top right
            if (x.last < input[0].length - 1 && input[y - 1][x.last + 1].isPart()) {
                locations.add(Location(x = x.last + 1, y = y - 1))
            }
        }
        // Check bottom
        if (y < input.size - 1) {
            for (i in x) {
                if (input[y + 1][i].isPart()) {
                    locations.add(Location(x = i, y = y + 1))
                }
            }
            // Check bottom left
            if (x.first > 0 && input[y + 1][x.first - 1].isPart()) {
                locations.add(Location(x = x.first - 1, y = y + 1))
            }
            // Check bottom right
            if (x.last < input[0].length - 1 && input[y + 1][x.last + 1].isPart()) {
                locations.add(Location(x = x.last + 1, y = y + 1))
            }
        }
        // Check left
        if (x.first > 0 && input[y][x.first - 1].isPart()) {
            locations.add(Location(x = x.first - 1, y = y))
        }
        // Check right
        if (x.last < input[0].length - 1 && input[y][x.last + 1].isPart()) {
            locations.add(Location(x = x.last + 1, y = y))
        }
        return locations
    }

    fun isPartNumber(input: List<String>, y: Int, x: IntRange): Boolean {
        return getNeighbourParts(input = input, y = y, x = x).isNotEmpty()
    }

    fun String.findPartNumbers(y: Int, input: List<String>): Int {
        val numberParser = """(\d+)""".toRegex()
        val numberGroups = numberParser.findAll(this)
        return numberGroups.map { matchResult ->
            matchResult.let {
                if (isPartNumber(input = input, y = y, x = it.range)) {
                    it.value.toInt()
                } else {
                    0
                }
            }
        }.sum()
    }

    fun part1(input: List<String>): Int {
        return input.mapIndexed { index, row -> row.findPartNumbers(y = index, input = input) }.sum()
    }

    fun String.findGearRatios(y: Int, input: List<String>, partToGearRatios: MutableMap<Location, List<Int>>) {
        val numberParser = """(\d+)""".toRegex()
        val numberGroups = numberParser.findAll(this)
        numberGroups.forEach { matchResult ->
            getNeighbourParts(input = input, y = y, x = matchResult.range).forEach {
                partToGearRatios[it] = partToGearRatios.getOrDefault(it, listOf()) + matchResult.value.toInt()
            }
        }
    }

    fun part2(input: List<String>): Int {
        val partToGearRatios = mutableMapOf<Location, List<Int>>()
        input.forEachIndexed { index, row -> row.findGearRatios(y = index, input = input, partToGearRatios = partToGearRatios) }
        val gears = partToGearRatios.filter { it.value.size == 2 }
        return gears.map { it.value.reduce { acc, i -> acc * i } }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    // test if implementation meets criteria from the description, like:
    val testInput2 = readInput("Day03_test")
    check(part2(testInput2) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
