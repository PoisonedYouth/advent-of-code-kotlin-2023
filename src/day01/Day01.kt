package day01

import println
import readInput

private enum class Digits(val intValue: Int) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9)
}

private data class DigitsEntry(
    val digits: Digits,
    val firstDigitIndex: Int,
    val lastDigitIndex: Int
)

private fun createWordIndexMapFor(line: String): List<DigitsEntry> = buildList {
    Digits.entries.forEach { digits ->
        val result = Regex(digits.name.lowercase()).findAll(line).map { it.range.first }.toList()
        if (result.isNotEmpty()) {
            add(
                DigitsEntry(
                    digits = digits,
                    firstDigitIndex = result.first(),
                    lastDigitIndex = result.last()
                )
            )
        }
    }
}

private fun List<DigitsEntry>.getMin(): Int {
    return this.minBy {
        it.firstDigitIndex
    }.digits.intValue
}

private fun List<DigitsEntry>.getMinIndex(): Int {
    return this.minBy {
        it.firstDigitIndex
    }.firstDigitIndex
}

private fun List<DigitsEntry>.getMax(): Int {
    return this.maxBy {
        it.lastDigitIndex
    }.digits.intValue
}

private fun List<DigitsEntry>.getMaxIndex(): Int {
    return this.maxBy {
        it.lastDigitIndex
    }.lastDigitIndex
}

private fun findFirstDigit(
    indexOfWords: List<DigitsEntry>,
    line: String,
    index: Int
): Int = when {
    indexOfWords.isEmpty() -> line[index].digitToInt()
    index == -1 -> indexOfWords.getMin()
    indexOfWords.getMinIndex() < index -> indexOfWords.getMin()
    else -> line[index].digitToInt()
}

private fun findLastDigit(
    indexOfWords: List<DigitsEntry>,
    line: String,
    index: Int
): Int = when {
    indexOfWords.isEmpty() -> line[index].digitToInt()
    index == -1 -> indexOfWords.getMax()
    indexOfWords.getMaxIndex() > index -> indexOfWords.getMax()
    else -> line[index].digitToInt()
}


fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line.filter { character -> character.isDigit() }
                .let { "${it.first()}${it.last()}" }
                .toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val indexOfWords = createWordIndexMapFor(line)
            val firstIndexInt = line.indexOfFirst { it.isDigit() }
            val lastIndexInt = line.indexOfLast { it.isDigit() }

            val first = findFirstDigit(indexOfWords, line, firstIndexInt)
            val last = findLastDigit(indexOfWords, line, lastIndexInt)

            "$first$last".toInt()
        }
    }

    val input = readInput("day01/Day01")
    part1(input).println()
    part2(input).println()
}
