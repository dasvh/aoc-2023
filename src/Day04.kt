import kotlin.math.pow

fun main() {
    fun part1and2(input: List<String>) {
        var part1 = 0
        val part2 = MutableList(input.size) { 1 }

        for (i in input.indices) {
            val content = input[i].substringAfter(":").trim()
            val numbersString = content.split("|").map { it.trim() }

            val winningNumbers = numbersString[0].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
            val scratchNumbers = numbersString[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }

            val matchingNumbersCount = scratchNumbers.intersect(winningNumbers.toSet()).size

            val points = if (matchingNumbersCount > 0) 2.0.pow((matchingNumbersCount - 1).toDouble()).toInt() else 0
            part1 += points

            for (j in i + 1..i + matchingNumbersCount) {
                part2[j] += part2[i]
            }
        }

        println(part1)
        println(part2.sum())
    }

    val input = readInput("Day04")
    part1and2(input).println()
}