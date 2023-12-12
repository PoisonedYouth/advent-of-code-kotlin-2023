package day12

import readInput
import kotlin.math.min
import kotlin.system.measureTimeMillis

private data class Record(
    val springs: List<Char>,
    val damaged: List<Int>
)

private fun findCombinations(springs: List<Char>, damaged: List<Int>): Long {
    buildMap {
        fun recursive(springs: List<Char>, damaged: List<Int>): Long = getOrPut(springs to damaged) {
            when (val damage = damaged.firstOrNull()) {
                null -> if (springs.none { it == '#' }) 1 else 0
                else -> {
                    val maxIndex = min(
                        springs.indexOf('#').takeIf { it >= 0 } ?: Int.MAX_VALUE,
                        springs.size - damaged.drop(1).sum() - damaged.size + 1 - damage,
                    )
                    (0..maxIndex).sumOf { index ->
                        val piece = springs.subList(index, index + damage)
                        when (piece.none { it == '.' } && springs.getOrNull(index + damage) != '#') {
                            true -> recursive(springs.drop(index + damage + 1), damaged.drop(1))
                            else -> 0
                        }
                    }
                }
            }
        }
        return recursive(springs, damaged)
    }
}

private fun List<String>.toRecords(repeatInput: Int = 1): List<Record> {
    return this.map { line ->
        val chars = line.substringBefore(" ").map { it }
        val newChars = chars.toMutableList()

        repeat(repeatInput - 1) {
            newChars.add('?')
            newChars.addAll(chars)
        }
        val values = line.substringAfter(" ").split(",").map { it.toInt() }
        val newValues = values.toMutableList()
        repeat(repeatInput - 1) {
            newValues.addAll(values)
        }
        Record(
            springs = newChars.toList(),
            damaged = newValues.toList()
        )
    }
}

fun main() {

    fun part1(input: List<String>): Long {
        val sum = input.toRecords().sumOf { line ->
            findCombinations(line.springs, line.damaged)
        }
        return sum.also {
            check(it == 7344L) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    fun part2(input: List<String>): Long {
        val sum = input.toRecords(5).sumOf { line ->
            findCombinations(line.springs, line.damaged)
        }

        return sum.also {
            check(it == 1088006519007) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    val input = readInput("day12/Day12")
    measureTimeMillis {
        println(part1(input))
    }.also { println("Part 1 took: $it nano seconds.") }
    measureTimeMillis {
        println(part2(input))
    }.also { println("Part 2 took: $it nano seconds.") }
}