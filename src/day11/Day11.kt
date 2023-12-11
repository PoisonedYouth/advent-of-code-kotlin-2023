package day11

import readInput
import kotlin.math.abs
import kotlin.system.measureTimeMillis

private data class Point(
    val x: Int,
    val y: Int
) {

    private fun getLowerX(other: Point): Int {
        return if (this.x > other.x) {
            other.x
        } else {
            this.x
        }
    }

    private fun getUpperX(other: Point): Int {
        return if (this.x > other.x) {
            this.x
        } else {
            other.x
        }
    }

    private fun getLowerY(other: Point): Int {
        return if (this.y > other.y) {
            other.y
        } else {
            this.y
        }
    }

    private fun getUpperY(other: Point): Int {
        return if (this.y > other.y) {
            this.y
        } else {
            other.y
        }
    }

    fun distance(other: Point, expandedColumns: List<Int>, expandedRows: List<Int>): Long {
        val countRows = expandedRows.count { it in getLowerY(other)..getUpperY(other) }
        val countColumns = expandedColumns.count { it in getLowerX(other)..getUpperX(other) }
        return abs(x - other.x).toLong() + abs(y - other.y) + countRows + countColumns
    }

    fun distanceExtended(other: Point, expandedRows: List<Int>, expandedColumns: List<Int>): Long {
        val countColumns = expandedColumns.count { it in getLowerY(other)..getUpperY(other) } * 999_999
        val countRows = expandedRows.count { it in getLowerX(other)..getUpperX(other) } * 999_999
        return abs(x - other.x).toLong() + abs(y - other.y) + countRows + countColumns
    }
}

private fun List<String>.extractGalaxies(): List<Point> {
    val galaxies = mutableListOf<Point>()
    this.forEachIndexed { y: Int, row: String ->
        row.forEachIndexed { x: Int, c: Char ->
            if (c == '#') galaxies += Point(x, y)
        }
    }
    return galaxies
}

private fun List<String>.getExpandedColumns(galaxies: List<Point>): List<Int> =
    (0..<(this[0].length)).filter { x -> (0..<(this.size)).none { y -> Point(x, y) in galaxies } }

private fun List<String>.getExpandedRows(galaxies: List<Point>): List<Int> =
    (0..<(this.size)).filter { y -> (0..<(this[0].length)).none { x -> Point(x, y) in galaxies } }

private fun calculateSum(
    input: List<String>,
    distanceCalculationFunction: Point.(Point, List<Int>, List<Int>) -> Long
): Long {
    val galaxies = input.extractGalaxies()

    val expandedColumns = input.getExpandedColumns(galaxies)
    val expandedRows = input.getExpandedRows(galaxies)

    val pairs = mutableSetOf<Pair<Point, Point>>()

    for (i in galaxies.indices) {
        for (j in i+1 until galaxies.size) {
            pairs.add(galaxies[i] to galaxies[j])
        }
    }
    return pairs.sumOf {
        it.first.distanceCalculationFunction(it.second, expandedColumns, expandedRows)
    }
}

fun main() {

    fun part1(input: List<String>): Long {
        return calculateSum(input, Point::distance).also {
            check(it == 9445168L) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    fun part2(input: List<String>): Long {
        return calculateSum(input, Point::distanceExtended).also {
            check(it == 742305960572L) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    val input = readInput("day11/Day11")
    measureTimeMillis {
        println(part1(input))
    }.also { println("Part 1 took: $it nano seconds.") }
    measureTimeMillis {
        println(part2(input))
    }.also { println("Part 2 took: $it nano seconds.") }
}