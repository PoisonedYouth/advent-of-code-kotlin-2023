package day06

import println
import readInput
import kotlin.system.measureTimeMillis

private val NUMBER_REGEX = """\d+""".toRegex()


private data class Race(
    val time: Int,
    val distance: Int
)

fun main() {
    fun parseInput(input: List<String>): List<Race> {
        val timeValues = NUMBER_REGEX.findAll(input[0]).map { it.value.toInt() }.toList()
        val distanceValues = NUMBER_REGEX.findAll(input[1]).map { it.value.toInt() }.toList()
        require(timeValues.size == distanceValues.size) {
            "Wrong input."
        }
        return timeValues.zip(distanceValues) { time, distance ->
            Race(
                time = time,
                distance = distance
            )
        }
    }

    fun part1(input: List<String>): Int {
        return parseInput(input)
            .map {  (time, distance) -> (0..time).count { speed -> (time - speed) * speed > distance } }
            .reduce{acc, v -> acc * v}
            .also {
                check(it == 6209190) {
                    "Expected result is not equal to current result '$it'"
                }
            }
    }

    fun part2(input: List<String>): Long {
        val time = NUMBER_REGEX.findAll(input[0]).map { it.value }.joinToString("").toLong()
        val distance = NUMBER_REGEX.findAll(input[1]).map { it.value }.joinToString("").toLong()
        return (0..time).count { speed -> (time - speed) * speed > distance }.toLong().also {
            check(it == 28545089L) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }


    val input = readInput("day06/Day06")
    measureTimeMillis {
        part1(input).println()
    }.also { println("Part 1 took: $it nano seconds.") }
    measureTimeMillis {
        part2(input).println()
    }.also { println("Part 2 took: $it nano seconds.") }
}
