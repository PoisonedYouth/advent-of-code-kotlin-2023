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

    val getLocation: (List<Mapping>, List<Long>) -> List<Long> = { mappings, seeds ->
        val values = seeds.toMutableList()
        mappings.forEach { mapping ->
            values.forEachIndexed { i, seed ->
                val result = mapping.ranges.minOf {
                    if (seed in it.first) {
                        val newValue = values[i] - it.first.first
                        it.second.first + newValue
                    } else {
                        Long.MAX_VALUE
                    }
                }
                if (result < Long.MAX_VALUE) {
                    values[i] = result
                }
            }
        }
        values
    }


    fun part1(input: List<String>): Long {
        val result = extractMap(input)

        val seeds = NUMBER_REGEX.findAll(input.first()).map { it.value.toLong() }.toList()
        val location = getLocation(result, seeds)

        return location.min().also {
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
                it.chunked(1000){ chunk ->
                    val location = getLocation(result, chunk.toList())
                    if (minLoc > location.min())
                        minLoc = location.min()
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
