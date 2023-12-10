package day10

import day10.Direction.EAST
import day10.Direction.NORTH
import day10.Direction.SOUTH
import day10.Direction.WEST
import readInput
import kotlin.system.measureTimeMillis

private data class Point(
    val x: Int,
    val y: Int
) {

    fun getAdjacentSides(): List<Point> = listOf(
        Point(x, y - 1), Point(x - 1, y), Point(x + 1, y), Point(x, y + 1),
    )

    fun getAdjacent(): List<Point> = listOf(
        Point(x - 1, y - 1), Point(x, y - 1), Point(x + 1, y - 1),
        Point(x - 1, y), Point(x + 1, y),
        Point(x - 1, y + 1), Point(x, y + 1), Point(x + 1, y + 1),
    )

    fun move(direction: Direction): Point {
        return when (direction) {
            NORTH -> Point(
                x = this.x,
                y = this.y - 1
            )

            SOUTH -> Point(
                x = this.x,
                y = this.y + 1
            )

            EAST -> Point(
                x = this.x + 1,
                y = this.y
            )

            WEST -> Point(
                x = this.x - 1,
                y = this.y
            )
        }
    }
}

private enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    fun reverse(): Direction {
        return when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
    }
}

private val directionMap = mapOf(
    'S' to listOf(NORTH, EAST, SOUTH, WEST),
    '|' to listOf(SOUTH, NORTH),
    '-' to listOf(WEST, EAST),
    'L' to listOf(NORTH, EAST),
    'J' to listOf(NORTH, WEST),
    '7' to listOf(SOUTH, WEST),
    'F' to listOf(SOUTH, EAST)
)

private fun List<String>.toGrid(): Map<Point, Char> = buildMap {
    this@toGrid.forEachIndexed { y: Int, row: String ->
        row.forEachIndexed { x: Int, c: Char ->
            put(Point(x, y), c)
        }
    }
}

private fun <K, V> Map<K, V>.getOrThrow(k: K): V {
    return this[k] ?: error("Key '$k' not found!")
}

fun main() {

    fun part1(input: List<String>): Int {
        val grid = input.toGrid()

        val start = grid.entries.first { it.value == 'S' }.key
        val unexplored = mutableListOf(start to 0)
        val explored = mutableMapOf(start to 0)

        while (unexplored.isNotEmpty()) {
            val (current, distance) = unexplored.removeFirst()
            explored[current] = distance
            val directions = directionMap.getOrThrow(grid.getOrThrow(current))
            directions.forEach { direction ->
                val point = current.move(direction)
                if (point !in explored.keys && point in grid.keys && direction.reverse() in directionMap.getOrThrow(
                        grid.getOrThrow(
                            point
                        )
                    )
                ) {
                    unexplored += point to (distance + 1)
                }
            }

        }

        return explored.values.max().also {
            check(it == 6907) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    fun part2(input: List<String>): Int {
        val grid = input.toGrid()

        val start = grid.entries.first { it.value == 'S' }.key
        val unexplored = mutableListOf(start)
        val explored = mutableSetOf<Point>()
        while (unexplored.isNotEmpty()) {
            val current = unexplored.removeFirst()
            explored += current
            val directions = directionMap.getOrThrow(grid.getOrThrow(current))
            directions.forEach { direction ->
                val point = current.move(direction)
                if (point !in explored) {
                    val symbol = grid[point]
                    if (symbol != null && direction.reverse() in directionMap.getOrThrow(symbol)) {
                        unexplored += point
                    }
                }
            }
        }

        val expandedGrid = mutableMapOf<Point, Char>()
        grid.forEach { (point, char) ->
            val expandedPoint = Point(point.x * 2, point.y * 2)
            expandedGrid[expandedPoint] = if (point in explored) {
                '0'
            } else {
                'I'
            }
            expandedPoint.getAdjacent().forEach { expandedGrid[it] = 'I' }
            if (point in explored) directionMap.getOrThrow(char).forEach { expandedGrid[expandedPoint.move(it)] = '0' }
        }

        val toFlood = mutableListOf(Point(0, 0))
        while (toFlood.isNotEmpty()) {
            val current = toFlood.removeFirst()
            expandedGrid[current] = '0'
            toFlood += current.getAdjacentSides().filter { expandedGrid[it] == 'I' && it !in toFlood }
        }

        return grid.keys.count { expandedGrid[Point(it.x * 2, it.y * 2)] == 'I' }.also {
            check(it == 541) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    val input = readInput("day10/Day10")
    measureTimeMillis {
        println(part1(input))
    }.also { println("Part 1 took: $it nano seconds.") }
    measureTimeMillis {
        println(part2(input))
    }.also { println("Part 2 took: $it nano seconds.") }
}