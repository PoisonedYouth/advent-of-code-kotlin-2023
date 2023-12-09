package day08

import readInput
import kotlin.system.measureTimeMillis


private data class Node(
    val starting: String,
    val left: String,
    val right: String
) {
    init {
        require(starting.length == 3) {
            "Starting must be 3 characters long"
        }
        require(left.length == 3) {
            "Left must be 3 characters long"
        }
        require(right.length == 3) {
            "Right must be 3 characters long"
        }
    }
}

private data class Instructions(
    val operations: List<Operation>
) {
    fun nextOperation(step: Int): Operation {
        return operations[step % operations.size]
    }
}

private enum class Operation(val value: Char) {
    LEFT('L'),
    RIGHT('R')
}

private fun parseInput(input: List<String>): Pair<Instructions, List<Node>> {
    val instructions = Instructions(
        operations = input[0].map { char -> Operation.entries.first { it.value == char } }
    )
    val nodes: List<Node> = input.drop(2).map { line: String ->
        val parts = line.split(" = ")
        Node(
            starting = parts[0],
            left = parts[1].substring(1, 4),
            right = parts[1].substring(6, 9)
        )
    }
    return instructions to nodes
}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun main() {

    fun part1(input: List<String>): Int {
        val game = parseInput(input)

        val instructions = game.first
        val nodes = game.second
        var currentNode = nodes.first { it.starting == "AAA" }
        var steps = 0
        while (currentNode.starting != "ZZZ") {
            val operation = instructions.nextOperation(steps)
            currentNode = when (operation) {
                Operation.LEFT -> nodes.first { it.starting == currentNode.left }
                Operation.RIGHT -> nodes.first { it.starting == currentNode.right }
            }
            steps++
        }

        return steps.also {
            check(it == 16409) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    fun part2(input: List<String>): Long {
        val game = parseInput(input)

        val instructions = game.first
        val nodes = game.second
        val currentNodes = nodes.filter { it.starting.endsWith("A") }
        val totalSteps = mutableListOf<Long>()
        currentNodes.forEach { node ->
            var currentNode = node
            var steps = 0
            while (!currentNode.starting.endsWith("Z")) {
                val operation = instructions.nextOperation(steps)
                currentNode = when (operation) {
                    Operation.LEFT -> nodes.first { it.starting == currentNode.left }
                    Operation.RIGHT -> nodes.first { it.starting == currentNode.right }
                }
                steps++
            }
            totalSteps.add(steps.toLong())
        }

        return totalSteps.reduce { a, b -> findLCM(a, b) }.also {
            check(it == 11795205644011L) {
                "Expected result is not equal to current result '$it'"
            }
        }
    }

    val input = readInput("day09/Day08")
    measureTimeMillis {
        println(part1(input))
    }.also { println("Part 1 took: $it nano seconds.") }
    measureTimeMillis {
        println(part2(input))
    }.also { println("Part 2 took: $it nano seconds.") }
}