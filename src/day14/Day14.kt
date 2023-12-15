package day14

import readInput
import kotlin.system.measureTimeMillis

fun main() {

    fun Iterable<String>.transpose(): List<String> = buildList {
        val strings = this@transpose.filterTo(mutableListOf()) { it.isNotEmpty() }
        var i = 0
        while (strings.isNotEmpty()) {
            add(buildString(strings.size) { for (string in strings) append(string[i]) })
            i++
            strings.removeAll { it.length == i }
        }
    }

    val N = 1000000000

    fun List<String>.tilt(): List<String> = map { StringBuilder(it) }.apply {
        for (x in first().indices) {
            var y0 = 0
            while (y0 < size) {
                var n = 0
                var y1 = y0
                do {
                    val c = this[y1][x]
                    if (c == 'O') n++
                } while (c != '#' && ++y1 < size)
                for (y in y0 until y1) this[y][x] = if (y < y0 + n) 'O' else '.'
                y0 = y1 + 1
            }
        }
    }.map { it.toString() }

    fun List<String>.spin(): List<String> = this
        .tilt().asReversed().transpose()
        .tilt().asReversed().transpose()
        .tilt().asReversed().transpose()
        .tilt().asReversed().transpose()

    fun List<String>.load(): Int = foldIndexed(0) { i, acc, line ->
        acc + (size - i) * line.count { it == 'O' }
    }


    fun part1(lines: List<String>): Int {
        return with(lines) {
            val width = fold(0) { acc, line -> maxOf(acc, line.length) }
            mapNotNull { it.ifEmpty { null }?.padEnd(width, '.') }.ifEmpty { listOf("") }
        }.tilt().load()

    }


    fun part2(lines: List<String>): Int {
        var state = with(lines) {
            val width = fold(0) { acc, line -> maxOf(acc, line.length) }
            mapNotNull { it.ifEmpty { null }?.padEnd(width, '.') }.ifEmpty { listOf("") }
        }
        val cache = mutableMapOf(state to 0)
        for (i in 1..N) {
            state = state.spin()
            val j = cache.getOrPut(state) { i }
            if (i != j) {
                repeat((N - i) % (i - j)) { state = state.spin() }
                break
            }
        }
        return state.load()
    }


    val input = readInput("day14/Day14")
    measureTimeMillis {
        println(part1(input))
    }.also { println("Part 1 took: $it nano seconds.") }
    measureTimeMillis {
        println(part2(input))
    }.also { println("Part 2 took: $it nano seconds.") }
}