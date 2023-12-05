fun main() {
    val input = readText("Day05").split("\n\n")
    val seeds = input.first().substringAfter(": ").split(" ").map(String::toLong)

    val maps = input.drop(1).map { map ->
        map.split("\n").drop(1).map { mapContent ->
            val (to, from, len) = mapContent.split(" ").map { it.toLong() }
            from..<(from + len) to (to - from)
        }
    }

    fun mapSeeds(seeds: Sequence<Long>) = seeds.map { seed ->
        fun useMap(item: Long, map: List<Pair<LongRange, Long>>): Long {
            for (mapping in map) {
                if (item in mapping.first) {
                    return item + mapping.second
                }
            }
            return item
        }
        maps.fold(seed) { item, map -> useMap(item, map) }
    }.min()

    val part1 = mapSeeds(seeds.asSequence())

    val part2 = seeds.chunked(2) { (a, b) ->
        println("Processing chunk: $a, $b")
        mapSeeds((a until a + b).asSequence())
    }.min()

    println(part1)
    println(part2)
}