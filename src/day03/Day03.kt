package day03

import println
import readInput

private val NUMBER_REGEX = """\d+""".toRegex()

private fun Char.isSymbol(): Boolean = this != '.' && !isDigit()

private data class NumberWithSymbol(
    val number: Int,
    val symbol: Pair<Int, Int>
)

private fun createResultList(input: List<String>): List<NumberWithSymbol> {
    val resultList = mutableSetOf<NumberWithSymbol>()
    input.forEachIndexed() { index, line ->
        val numbers = NUMBER_REGEX.findAll(line).map { it.value.toInt() to it.range }
        numbers.forEach { numberWithIndex ->
            // Checking line above
            if (index > 0) {
                val (startIndex, indexColumn) = findMatching(numberWithIndex, input, index - 1, line)
                if (indexColumn > -1) {
                    resultList.add(
                        NumberWithSymbol(
                            number = numberWithIndex.first,
                            symbol = index - 1 to indexColumn + startIndex
                        )
                    )
                }
            }
            // Checking line below
            if (index < input.size - 1) {
                val (startIndex, indexColumn) = findMatching(numberWithIndex, input, index + 1, line)
                if (indexColumn > -1) {
                    resultList.add(
                        NumberWithSymbol(
                            number = numberWithIndex.first,
                            symbol = index + 1 to indexColumn + startIndex
                        )
                    )
                }
            }
            // Checking same line
            val (startIndex, indexColumn) = findMatching(numberWithIndex, input, index, line)
            if (indexColumn > -1) {
                resultList.add(
                    NumberWithSymbol(
                        number = numberWithIndex.first,
                        symbol = index to indexColumn + startIndex
                    )
                )
            }
        }
    }
    return resultList.toList()
}

private fun findMatching(
    numberWithIndex: Pair<Int, IntRange>,
    input: List<String>,
    index: Int,
    line: String
): Pair<Int, Int> {
    val startIndex = (numberWithIndex.second.first - 1).coerceAtLeast(0)
    val indexColumn = input[index].substring(
        startIndex,
        (numberWithIndex.second.last + 2).coerceAtMost(line.length)
    ).indexOfFirst { it.isSymbol() }
    return Pair(startIndex, indexColumn)
}

fun main() {
    fun part1(input: List<String>): Int {
        return createResultList(input).sumOf { it.number }
    }

    fun part2(input: List<String>): Int {
        return createResultList(input).groupBy { it.symbol }.map {
            if (it.value.size > 1) {
                it.value.fold(1) { acc, n -> acc * n.number }
            } else {
                0
            }
        }.sum()
    }

    val input = readInput("day03/Day03")
    part1(input).println()
    part2(input).println()
}
