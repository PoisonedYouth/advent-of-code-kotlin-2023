package day15

import readInput
import kotlin.system.measureTimeMillis

private enum class Type {
    DASH,
    EQUALS
}

private data class Step(
    val key: String,
    val type: Type
) {
    companion object {
        fun parse(value: String): Step {
            val type = when {
                value.contains("=") -> Type.EQUALS
                else -> Type.DASH
            }
            return Step(
                key = value.split("-", "=").first(),
                type = type
            )
        }
    }
}

fun main() {
    fun parseInput(lines: List<String>): List<String> {
        return lines.joinToString().split(",")
    }

    fun String.hash() = fold(0) { acc, c ->
        ((acc + c.code) * 17).mod(256)
    }

    fun part1(lines: List<String>): Int {
        return parseInput(lines).sumOf { step ->
            step.hash()
        }.also {
            check(it == 515974) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    fun part2(lines: List<String>): Int {
        val input = parseInput(lines)
        val boxes = MutableList(256) { mutableMapOf<String, Int>() }
        for (stepString in input) {
            val step = Step.parse(stepString)
            if (step.type == Type.DASH) {
                boxes[step.key.hash()].remove(step.key)
            } else {
                boxes[step.key.hash()][step.key] = stepString.split("=").last().toInt()
            }
        }
        return boxes.withIndex().sumOf { (i, bucket) ->
            (i + 1) * bucket.values.withIndex().sumOf { (j, value) -> (j + 1) * value }
        }.also {
            check(it == 265894) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }


    val input = readInput("day15/Day15")
    measureTimeMillis {
        println(part1(input))
    }.also { println("Part 1 took: $it nano seconds.") }
    measureTimeMillis {
        println(part2(input))
    }.also { println("Part 2 took: $it nano seconds.") }
}