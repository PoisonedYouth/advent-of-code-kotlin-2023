package day05

import println
import readInput
import kotlin.system.measureTimeMillis

private val NUMBER_REGEX = """\d+""".toRegex()

private data class Mapping(
    val index: Int,
    val ranges: List<Pair<LongRange, LongRange>>
)

fun main() {
    val extractMap: (List<String>) -> List<Mapping> = { list ->
        val result = mutableListOf<Mapping>()
        var mappingIndex = 0
        var mapping = Mapping(
            index = mappingIndex,
            ranges = emptyList()
        )
        list.drop(3).forEachIndexed { index, line ->
            if (line.isEmpty()) return@forEachIndexed
            mapping = if (NUMBER_REGEX.find(line) == null || index == list.size - 4) {
                result.add(mapping)
                Mapping(
                    index = ++mappingIndex,
                    ranges = emptyList()
                )
            } else {
                val (dest, src, range) = line.split(" ").map { it.trim().toLong() }
                mapping.copy(
                    ranges = mapping.ranges + Pair(src..<src + range, dest..<dest + range)
                )
            }

        }
        result.toList()
    }

    val searchValueNew: (List<Mapping>, Long) -> Long = { mappings, seeds ->
        var value = seeds
        mappings.forEach { mapping ->
            value = mapping.ranges.minOf {
                if (value in it.first) {
                    val index = value - it.first.first
                    it.second.first + index
                } else {
                    Long.MAX_VALUE
                }
            }
        }
        value
    }

    fun getLocation(result: List<Mapping>, seed: Long): Long {
        return searchValueNew(result, seed)
    }

    fun part1(input: List<String>): Long {
        val result = extractMap(input)

        var minLoc = Long.MAX_VALUE
        val seeds = NUMBER_REGEX.findAll(input.first()).map { it.value.toLong() }
        seeds.forEach { seed ->
            val location = getLocation(result, seed)
            if (minLoc > location)
                minLoc = location
        }
        return minLoc.also {
            check(it == 389056265L) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    fun part2(input: List<String>): Long {
        val result = extractMap(input)

        val seedsRange = NUMBER_REGEX.findAll(input.first())
            .map { it.value.toLong() }
            .chunked(2).map {
                it.first()..<it.first() + it.last()
            }.toList()
        var minLoc = Long.MAX_VALUE
        seedsRange.forEach {
            for (seed in it.first..it.last) {
                val location = getLocation(result, seed)
                if (minLoc > location)
                    minLoc = location
            }
        }
        return minLoc.also {
            check(it == 137516820L) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    val input = readInput("day05/Day05")
    measureTimeMillis {
        part1(input).println()
    }.also { println("Part 1 took: $it nano seconds.") }
    measureTimeMillis {
        part2(input).println()
    }.also { println("Part 2 took: $it nano seconds.") }
}
