fun main() {
    println(Day07.part1(readInput("Day07")))
    println(Day07.part2(readInput("Day07")))
}

object Day07 {
    private data class Hand(val cards: List<Char>, val bid: Int) : Comparable<Hand> {
        private val strength = listOf('-', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
                .withIndex()
                .associate { it.value to it.index }

        private val counts = cards.groupingBy { it }.eachCount()
        private val countsWithoutJokers = counts.filterKeys { it != '-' }.values.sortedDescending()

        override fun compareTo(other: Hand): Int {
            val handComparison = compareBy<Hand> { (it.countsWithoutJokers.firstOrNull() ?: 0) + (it.counts['-'] ?: 0) }
                    .thenBy { it.countsWithoutJokers.drop(1).firstOrNull() ?: 0 }
                    .thenComparing { firstHand, secondHand ->
                        val (cardFromFirstHand, cardFromSecondHand) =
                                firstHand.cards.zip(secondHand.cards)
                                        .dropWhile { (cardFirst, cardSecond) -> cardFirst == cardSecond }
                                        .first()
                        strength.getValue(cardFromFirstHand).compareTo(strength.getValue(cardFromSecondHand))
                    }

            return handComparison.compare(this, other)
        }
    }

    private fun parse(line: String, mapper: Map<Char, Char>): Hand {
        val (hand, bid) = line.split(" ")

        return Hand(hand.map { mapper[it] ?: it }, bid.toInt())
    }

    private fun getBidReturns(lines: List<String>, mapper: Map<Char, Char> = mapOf()): Int =
            lines.map { parse(it, mapper) }
                    .sorted()
                    .mapIndexed { index, hand -> hand.bid * (index + 1) }
                    .sum()

    fun part1(lines: List<String>): Int = getBidReturns(lines)
    fun part2(lines: List<String>): Int = getBidReturns(lines, mapOf('J' to '-'))
}
