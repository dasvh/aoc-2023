fun main() {
    fun instructions(input: List<String>) = input.first()

    fun getMap(input: List<String>) = input.drop(2)
        .associate { line ->
            line.split(" = ", "(", ")", ",", " ").filter { it.isNotEmpty() }
                .let { parts -> parts.first() to parts.drop(1) }
        }

    fun steps(start: String, map: Map<String, List<String>>, direction: Char) =
        if (direction == 'L') map[start]!!.first() else map[start]!!.last()

    fun countSteps(start: String, map: Map<String, List<String>>, sequence: String, breakOut: (String) -> Boolean): Long {
        var current = start
        var steps = 0L
        while (breakOut(current)) {
            sequence.forEach { direction ->
                current = steps(current, map, direction)
                steps++
                if (current.endsWith('Z')) return steps
            }
        }
        return steps
    }

    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    fun lcd(a: Long, b: Long): Long = a * b / gcd(a, b)

    fun getRoutes(routes: List<String>, singleLetter: Boolean = false) =
        if (singleLetter) routes.filter { it.endsWith("A") } else listOf("AAA")

    fun getSteps(input: List<String>, singleLetter: Boolean = false, breakOut: (String) -> Boolean): List<Long> {
        val sequence = instructions(input)
        val map = getMap(input)

        return getRoutes(map.keys.toList(), singleLetter).map { start ->
            countSteps(start, map, sequence, breakOut)
        }
    }

    fun part1(input: List<String>) = getSteps(input) { it != "ZZZ" }.first()

    fun part2(input: List<String>) = getSteps(input, true) { !it.endsWith('Z') }
        .reduce { current, next -> lcd(current, next) }

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}