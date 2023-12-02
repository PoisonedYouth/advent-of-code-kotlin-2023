package day02

import println
import readInput

private class Game private constructor(
    val id: Int,
) {
    private val green = mutableListOf<Int>()
    private val red = mutableListOf<Int>()
    private val blue = mutableListOf<Int>()

    fun idIfPossibleOrZero(): Int {
        return if (red.max() <= 12 && green.max() <= 13 && blue.max() <= 14) id else 0
    }

    fun power(): Int {
        return red.max() * blue.max() * green.max()
    }

    companion object {
        fun parse(line: String): Game {
            val id = line.extractId()
            val game = Game(
                id = id
            )
            line.extractColorValuePairs().forEach { colorWithValue ->
                val color = colorWithValue.extractColor()
                val value = colorWithValue.extractValue()
                when (color) {
                    "red" -> game.red.add(value)
                    "blue" -> game.blue.add(value)
                    "green" -> game.green.add(value)
                    else -> error("Invalid color '$color'.")
                }
            }
            return game
        }

        private fun String.extractColorValuePairs() = this.substringAfter(": ").split(", ", "; ")

        private fun String.extractId() = this.substringBefore(":").substringAfter(" ").toInt()

        private fun String.extractColor() = this.substringAfter(" ")

        private fun String.extractValue() = this.substringBefore(" ").toInt()
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            Game.parse(line).idIfPossibleOrZero()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            Game.parse(line).power()
        }
    }

    val input = readInput("day02/Day02")
    part1(input).println()
    part2(input).println()
}
