fun main() {
    fun getReadings(input: List<String>): List<List<Int>> {
        return input.map { line ->
            line.split(" ").map { it.toInt() }
        }
    }

    fun getDifferences(readings: List<Int>): Int {
        return if (readings.all { it == 0 }) {
            0
        } else {
            val next = readings.zipWithNext().map { (previous, next) -> next - previous }

            return readings.last() + getDifferences(next)
        }
    }

    fun part1(input: List<String>): Int {
        return getReadings(input).sumOf { getDifferences(it) }
    }

    fun part2(input: List<String>): Int {
        return getReadings(input).map { it.reversed() }.sumOf { getDifferences(it) }
    }

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}