package day09

import readInput
import kotlin.system.measureTimeMillis

private enum class Card(val value: Char, val jokerOrder: Int) {
    A('A', 1),
    K('K', 2),
    Q('Q', 3),
    J('J', 13),
    T('T', 4),
    NINE('9', 5),
    EIGHT('8', 6),
    SEVEN('7', 7),
    SIX('6', 8),
    FIVE('5', 9),
    FOUR('4', 10),
    THREE('3', 11),
    TWO('2', 12)
}

private enum class Type {
    FIVE_A_KIND,
    FOUR_A_KIND,
    FULL_HOUSE,
    THREE_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}

private object StandardHandComparator : Comparator<Hand> {
    override fun compare(h1: Hand, h2: Hand): Int {
        val typeComparison = h1.type.compareTo(h2.type)
        return if (typeComparison != 0) typeComparison
        else compareCardValues(h1.cards, h2.cards) { card -> card.ordinal }
    }
}

private object JokerEnhancedHandComparator : Comparator<Hand> {
    override fun compare(h1: Hand, h2: Hand): Int {
        val typeComparison = h1.jokerType.compareTo(h2.jokerType)
        return if (typeComparison != 0) typeComparison
        else compareCardValues(h1.cards, h2.cards) { card -> card.jokerOrder }
    }
}

private fun <T : Comparable<T>> compareCardValues(
    cards1: List<Card>,
    cards2: List<Card>,
    valueSelector: (Card) -> T
): Int {

    for (i in cards1.indices) {
        val cardComparison = valueSelector(cards1[i])
            .compareTo(valueSelector(cards2[i]))
        if (cardComparison != 0) {
            return cardComparison
        }
    }
    return 0
}

private data class Hand(
    val cards: List<Card>,
    val bid: Int
) {
    init {
        require(cards.size == 5) {
            "Each hand can only have 5 cards."
        }
    }

    private fun List<Card>.determineType(): Type = when {
        this.distinct().size == 1 -> Type.FIVE_A_KIND
        this.groupBy { it }.values.any { it.size == 4 } -> Type.FOUR_A_KIND
        this.groupBy { it }.values.any { it.size == 3 } && this.groupBy { it }.values.any { it.size == 2 } -> Type.FULL_HOUSE
        this.groupBy { it }.values.any { it.size == 3 } -> Type.THREE_A_KIND
        this.groupBy { it }.values.filter { it.size == 2 }.size == 2 -> Type.TWO_PAIR
        this.groupBy { it }.values.any { it.size == 2 } -> Type.ONE_PAIR
        else -> Type.HIGH_CARD
    }

    val type: Type = cards.determineType()

    val jokerType: Type
        get() {
            val jokers = cards.filter { it == Card.J }
            val normalCards = cards.filter { it != Card.J }
            val type = normalCards.determineType()

            return if (type == Type.FIVE_A_KIND) {
                return type
            } else {
                applyBetterType(type, jokers)
            }
        }

    private fun applyBetterType(type: Type, jokers: List<Card>): Type {
        var improvedType = type
        repeat(jokers.size) {
            improvedType = when (improvedType) {
                Type.FIVE_A_KIND -> improvedType
                Type.FOUR_A_KIND -> Type.FIVE_A_KIND
                Type.FULL_HOUSE -> Type.FOUR_A_KIND
                Type.THREE_A_KIND -> Type.FOUR_A_KIND
                Type.TWO_PAIR -> Type.FULL_HOUSE
                Type.ONE_PAIR -> Type.THREE_A_KIND
                Type.HIGH_CARD -> Type.ONE_PAIR
            }
        }
        return improvedType
    }


    companion object {
        fun parse(line: String): Hand {
            val parts = line.split(" ")
            val cards = parts[0].map { cardString -> Card.entries.first { it.value == cardString } }
            val bid = parts[1].toInt()
            return Hand(cards, bid)
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val hands = input.map { Hand.parse(it) }.sortedWith(StandardHandComparator).reversed()
        return hands.mapIndexed { index, hand ->
            hand.bid * (index + 1)
        }.sum().also {
            check(it == 247823654) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    fun part2(input: List<String>): Int {
        val hands = input.map { Hand.parse(it) }.sortedWith(JokerEnhancedHandComparator).reversed()
        return hands.mapIndexed { index, hand ->
            hand.bid * (index + 1)
        }.sum().also {
            check(it == 245461700) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    val input = readInput("day07/Day07")
    measureTimeMillis {
        println(part1(input))
    }.also { println("Part 1 took: $it nano seconds.") }
    measureTimeMillis {
        println(part2(input))
    }.also { println("Part 2 took: $it nano seconds.") }
}