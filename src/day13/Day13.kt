package day13

import readInput
import kotlin.math.min
import kotlin.system.measureTimeMillis

private fun scan(grid: List<String>): Int? {
    return generateSequence(1 to emptyList<Int>()) { (start, list) ->
        if (start == -1) null else {
            val newList = if (grid[start] == grid[start - 1]) list.plusElement(start) else list
            val newStart = if (start == grid.lastIndex) -1 else start + 1
            newStart to newList
        }
    }.last().second.firstOrNull { foldIndexPlusOne ->
        val offset = min(grid.size - foldIndexPlusOne, foldIndexPlusOne) - 1
        (1..offset).all { shift ->
            val rightIndex = foldIndexPlusOne + shift
            val leftIndex = foldIndexPlusOne - 1 - shift
            grid[leftIndex] == grid[rightIndex]
        }
    }
}

private fun strDiff(a: String, b: String) =
    a.zip(b).count { it.first != it.second }


private fun scanDefect(grid: List<String>): Int? {
    return generateSequence(1 to emptyList<Int>()) { (start, list) ->
        if (start == -1) null else {
            val newList = if (strDiff(grid[start], grid[start - 1]) <= 1) list.plusElement(start) else list
            val newStart = if (start == grid.lastIndex) -1 else start + 1
            newStart to newList
        }
    }.last().second.firstOrNull { foldIndexPlusOne ->
        val offset = min(grid.size - foldIndexPlusOne, foldIndexPlusOne) - 1
        (0..offset).sumOf { shift ->
            val rightIndex = foldIndexPlusOne + shift
            val leftIndex = foldIndexPlusOne - 1 - shift
            strDiff(grid[leftIndex], grid[rightIndex])
        } == 1
    }
}

private fun transpose(lines: List<String>): List<String> {
    return (0..lines[0].lastIndex).fold(emptyList()) { acc, i ->
        acc.plusElement(lines.map { it[i] }.joinToString(""))
    }
}

private fun countMirrors(mirrors: List<Pair<Int, Int>>) =
    (100 * mirrors.sumOf { it.first } + mirrors.sumOf { it.second })

fun main() {
    fun List<String>.toGrid(): List<List<String>> = this.fold(listOf(emptyList())) { acc, s ->
        if (s.isEmpty()) {
            acc.plusElement(emptyList())
        } else {
            acc.dropLast(1).plusElement(acc.last() + listOf(s))
        }
    }


    fun part1(lines: List<String>): Long {
        val mirrors = lines.toGrid().map { grid ->
            (scan(grid) ?: 0) to (scan(transpose(grid)) ?: 0)
        }
        return countMirrors(mirrors).toLong().also {
            check(it == 36448L) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }


    fun part2(lines: List<String>): Long {
        val mirrors = lines.toGrid().map { grid ->
            (scanDefect(grid) ?: 0) to (scanDefect(transpose(grid)) ?: 0)
        }
        return countMirrors(mirrors).toLong().also {
            check(it == 35799L) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }


    val input = readInput("day13/Day13")
    measureTimeMillis {
        println(part1(input))
    }.also { println("Part 1 took: $it nano seconds.") }
    measureTimeMillis {
        println(part2(input))
    }.also { println("Part 2 took: $it nano seconds.") }
}