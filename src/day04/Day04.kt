package day04

import println
import readInput
import kotlin.system.measureTimeMillis

private val NUMBER_REGEX = """\d+""".toRegex()

data class Card(
    val id: Int,
    val winningNumbers: List<Int>,
    val myNumbers: List<Int>
) {
    val matchingNumbers = myNumbers.intersect(winningNumbers.toSet())

    fun calculateResult(): Int {
        return if (matchingNumbers.isEmpty()) {
            0
        } else {
            matchingNumbers.drop(1).fold(1) { acc, _ ->
                acc * 2
            }
        }
    }

    companion object {
        fun parse(line: String): Card {
            val id = NUMBER_REGEX.find(line.substringBefore(":"))?.value?.toInt() ?: error("Missing id.")
            val numbers = line.substringAfter(": ").split(" | ")
            val winningNumbers = NUMBER_REGEX.findAll(numbers[0]).map { it.value.toInt() }.toList()
            val myNumbers = NUMBER_REGEX.findAll(numbers[1]).map { it.value.toInt() }.toList()
            return Card(
                id = id,
                winningNumbers = winningNumbers,
                myNumbers = myNumbers

            )
        }
    }

}

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { Card.parse(it).calculateResult() }
    }

    fun part2(input: List<String>): Int {
        val cards = input.map { Card.parse(it) }
        val tmpCards = cards.toMutableList()
        var total = 0
        cards.forEach { card ->
            val cardId = card.id
            val winning = card.matchingNumbers.count()
            val matchingCardAmount = tmpCards.count { it.id == cardId }
            tmpCards.removeAll { it.id == cardId }
            val additionalWinningCards = cards.filter { it.id in cardId + 1..cardId + winning }
            repeat(matchingCardAmount) {
                tmpCards += additionalWinningCards
            }
            total += matchingCardAmount
        }
        return total
    }

    val input = readInput("day04/Day04")
    measureTimeMillis {
        part1(input).println()
    }.also { println("Part 1 took: $it nano seconds.") }
    measureTimeMillis {
        part2(input).println()
    }.also { println("Part 2 took: $it nano seconds.") }
}
