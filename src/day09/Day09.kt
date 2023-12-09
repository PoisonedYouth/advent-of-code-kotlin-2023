package day09

import readInput
import kotlin.system.measureTimeMillis


fun main() {

    fun List<Int>.calculateNext(): Int {
        var tmpList = this.toMutableList()
        val lastSteps = mutableListOf<Int>()
        while (tmpList.any { it != 0 }) {
            tmpList = tmpList.windowed(2) { (a, b) -> b - a }.toMutableList()
            lastSteps.add(tmpList.last())
        }
        return this.last() + lastSteps.sum()
    }

    fun part1(input: List<String>): Int {
        val parsedInput = input.map { line -> line.split(" ").map { it.toInt() } }
        return parsedInput.sumOf { it.calculateNext() }
            .also {
                check(it == 2098530125) {
                    "Expected result is not equal to current result '$it'"
                }
            }
    }

    fun part2(input: List<String>): Int {
        val parsedInput = input.map { line -> line.split(" ").map { it.toInt() } }
        return parsedInput.sumOf { it.reversed().calculateNext() }
            .also {
                check(it == 1016) {
                    "Expected result is not equal to current result '$it'"
                }
            }
    }

    val input = readInput("day09/Day09")
    measureTimeMillis {
        println(part1(input))
    }.also { println("Part 1 took: $it nano seconds.") }
    measureTimeMillis {
        println(part2(input))
    }.also { println("Part 2 took: $it nano seconds.") }
}