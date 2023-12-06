fun main() {
    fun getPossibleValues(time: Long, distance: Long): Int {
        return (1..time).count { (time - it) * it > distance }
    }

    fun part1(input: List<String>): Int{
        val (times, distances) = input.map { map ->
            map.split(":")
                    .last()
                    .split(" ")
                    .filter{ it.isNotBlank() }.map { it.toLong() }
        }

        return times.zip(distances).map { (time, distance) -> getPossibleValues(time, distance) }.reduce(Int::times)
    }

    fun part2(input: List<String>): Int{
        val (time, distance) = input.map { map ->
            map.split(":")
                    .last()
                    .filterNot { it.isWhitespace() }.toLong()
        }

        return getPossibleValues(time, distance)
    }

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}