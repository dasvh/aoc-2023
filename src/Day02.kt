fun main() {
    fun part1(input: List<String>): Int {
        val colourLimits = mapOf("red" to 12, "green" to 13, "blue" to 14)
        val matchingGames = mutableListOf<Int>()

        for (game in input) {
            val (gameId, content) = game.split(":", limit = 2)
            val subSet = content.split(";").map { it.trim() }
            var matchesCriteria = true

            for (colour in subSet) {
                val colourCounts = colour.split(",").map { it.trim() }
                val counts = mutableMapOf<String, Int>()

                for (colourCount in colourCounts) {
                    val (count, colour) = colourCount.split(" ")
                    counts[colour] = count.toInt()
                }

                for ((colour, limit) in colourLimits) {
                    if (counts.getOrDefault(colour, 0) > limit) {
                        matchesCriteria = false
                        break
                    }
                }

                if (!matchesCriteria) break
            }

            if (matchesCriteria) {
                matchingGames.add(gameId.trim().split(" ")[1].toInt())
            }
        }

        return matchingGames.sum()
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        var product: Int

        for (game in input) {
            val maxColour = mutableMapOf<String, Int>()
            val subSets = game.substringAfter(":").split(";").map { it.trim() }

            for (colour in subSets) {
                val colours = colour.split(",").map { it.trim() }

                for (colourCount in colours) {
                    val (countStr, colour) = colourCount.split(" ")
                    val count = countStr.toInt()
                    maxColour[colour] = maxOf(maxColour.getOrDefault(colour, 0), count)
                }
            }

            product = maxColour.values.fold(1) { colour, count -> colour * count }

            sum += product
        }

        return sum
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}