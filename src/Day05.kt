import java.lang.Long.max
import java.lang.Long.min

fun main() {
    data class LookUp(val sourceStart: Long, val destinationStart: Long, val range: Long) {
        fun contains(source: Long): Boolean {
            return source in sourceStart until (sourceStart + range)
        }

        fun toDestination(source: Long): Long {
            return destinationStart + (source - sourceStart)
        }

        fun toRangedDestination(source: LongRange): LongRange {
            val offset = destinationStart - sourceStart

            // If no intersection, return empty range
            if (source.last < sourceStart || source.first >= sourceStart + range) {
                return LongRange.EMPTY
            }

            return max(source.first, sourceStart) + offset until min(source.last + 1, sourceStart + range) + offset
            // If intersection, return the intersection, offset by destination
        }
    }
    fun sourceToDestination(source: List<Long>, lookup: List<String>): List<Long> {
        val lookups = lookup.map {
            val(destinationStart, sourceStart, range) = it.split(" ").map(String::toLong)
            LookUp(sourceStart = sourceStart, destinationStart = destinationStart, range = range)
        }
        return source.map {
            lookups.firstOrNull { lookup -> lookup.contains(it) }?.toDestination(it) ?: it
        }
    }

    fun rangedSourceToRangedDestination(source: List<Long>, lookup: List<String>): List<Long> {
        if (lookup.isEmpty()) {
            return source
        }
        val sourcePairs = source.chunked(2).sortedBy { it.first() }
        val sortedLookups = lookup.map {
            val(destinationStart, sourceStart, range) = it.split(" ").map(String::toLong)
            LookUp(sourceStart = sourceStart, destinationStart = destinationStart, range = range)
        }.sortedBy { it.sourceStart }

        // For each sourcePair, generate a list of intersecting lookups, and pad it with head/tail for non-intersecting
        return sourcePairs.map { (start, range) ->
            val tailStart = max(sortedLookups.last().sourceStart + sortedLookups.last().range, start)

            listOf<LongRange>(
                start until sortedLookups.first().sourceStart,
                tailStart until start + range,
                *(
                    sortedLookups.map {
                        it.toRangedDestination(start until start + range)
                    }
                    ).toTypedArray(),
            ).filter { it.last > it.first }.map {
                listOf(it.first, it.last - it.first + 1)
            }.flatten()
        }.flatten()
    }

    fun calculateLocation(
        seeds: List<Long>,
        input: List<String>,
        sourceToDestinationStrategy: (List<Long>, List<String>) -> List<Long>,
    ): List<Long> {
        val mapLines = mutableListOf<String>()
        var source = seeds
        input.forEach {
            if (it.isEmpty()) {
                return@forEach
            }
            // It's a new map!
            if (it.contains("map")) {
                // Convert source to destination and store as source for next map
                source = sourceToDestinationStrategy(source, mapLines)
                mapLines.clear()
                return@forEach
            }
            // Collect existing values
            mapLines.add(it)
        }
        // The final conversion
        return sourceToDestinationStrategy(source, mapLines)
    }

    fun part1(input: List<String>): Long {
        val seeds = input[0].substringAfter(":").trim().split(" ").map { it.toLong() }

        return calculateLocation(input = input.drop(1), seeds = seeds, sourceToDestinationStrategy = ::sourceToDestination).min()
    }

    fun part2(input: List<String>): Long {
        val seeds = input[0].substringAfter(":").trim().split(" ").map { it.toLong() }

        return calculateLocation(input = input.drop(1), seeds = seeds, sourceToDestinationStrategy = ::rangedSourceToRangedDestination).filterIndexed { index, _ ->
            index % 2 == 0
        }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)

    // test if implementation meets criteria from the description, like:
    val testInput2 = readInput("Day05_test")
    check(part2(testInput2) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
