
enum class Card {
    JOKER, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
}

fun Char.toCard(joker: Boolean = false): Card {
    return when (this) {
        '2' -> Card.TWO
        '3' -> Card.THREE
        '4' -> Card.FOUR
        '5' -> Card.FIVE
        '6' -> Card.SIX
        '7' -> Card.SEVEN
        '8' -> Card.EIGHT
        '9' -> Card.NINE
        'T' -> Card.TEN
        'J' -> if (!joker) Card.JACK else Card.JOKER
        'Q' -> Card.QUEEN
        'K' -> Card.KING
        'A' -> Card.ACE
        else -> throw IllegalArgumentException("Unknown card: $this")
    }
}

data class Hand(val cards: List<Card>) {
    fun score(): Int {
        val counter = if (cards.contains(Card.JOKER)) {
            val jokerlessHand = cards.filter { it != Card.JOKER }

            if (jokerlessHand.isEmpty()) {
                // 5 jokers = 5 of a kind
                return 7
            }
            val highFreqCard = jokerlessHand.groupingBy { it }.eachCount().maxBy { it.value }.key
            cards.map {
                if (it == Card.JOKER) {
                    highFreqCard
                } else {
                    it
                }
            }.groupingBy { it }.eachCount()
        } else {
            cards.groupingBy { it }.eachCount()
        }
        if (counter.size == 1) {
            // 5 of a kind
            return 7
        }
        if (counter.size == 2) {
            return if (counter.values.contains(4)) {
                // 4 of a kind
                6
            } else {
                // Full house
                5
            }
        }
        if (counter.size == 3) {
            return if (counter.values.contains(3)) {
                // 3 of a kind
                3
            } else {
                // 2 pairs
                2
            }
        }
        if (counter.size == 4) {
            // 1 pair
            return 1
        }
        // High card
        return 0
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        return input.map {
            val (hand, bid) = it.split(" ")
            Hand(hand.map { it.toCard() }) to bid.toLong()
        }.sortedWith(
            compareBy<Pair<Hand, Long>> { (hand, _) ->
                hand.score()
            }.thenBy { (hand, _) ->
                hand.cards[0].ordinal
            }.thenBy { (hand, _) ->
                hand.cards[1].ordinal
            }.thenBy { (hand, _) ->
                hand.cards[2].ordinal
            }.thenBy { (hand, _) ->
                hand.cards[3].ordinal
            }.thenBy { (hand, _) ->
                hand.cards[4].ordinal
            },
        ).map { (_, bid) ->
            bid
        }.reduceIndexed { index, acc, bid ->
            acc + bid * (index + 1)
        }
    }

    fun part2(input: List<String>): Long {
        return input.map {
            val (hand, bid) = it.split(" ")
            Hand(hand.map { it.toCard(joker = true) }) to bid.toLong()
        }.sortedWith(
            compareBy<Pair<Hand, Long>> { (hand, _) ->
                hand.score()
            }.thenBy { (hand, _) ->
                hand.cards[0].ordinal
            }.thenBy { (hand, _) ->
                hand.cards[1].ordinal
            }.thenBy { (hand, _) ->
                hand.cards[2].ordinal
            }.thenBy { (hand, _) ->
                hand.cards[3].ordinal
            }.thenBy { (hand, _) ->
                hand.cards[4].ordinal
            },
        ).map { (_, bid) ->
            bid
        }.reduceIndexed { index, acc, bid ->
            acc + bid * (index + 1)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)

    // test if implementation meets criteria from the description, like:
    val testInput2 = readInput("Day07_test")
    check(part2(testInput2) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
