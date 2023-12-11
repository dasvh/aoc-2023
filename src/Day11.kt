import kotlin.math.absoluteValue

data class Point(
    val x: Long,
    val y: Long,
    ) {
    fun manhattanDistance(other: Point): Long {
        return (x - other.x).absoluteValue + (y - other.y).absoluteValue
    }
}

private data class Square(
    val left: Long,
    val top: Long,
    val right: Long,
    val bottom: Long,
    )

fun main () {
    fun getGalaxies(input: List<String>): Set<Point> {
        val galaxies = mutableSetOf<Point>()

        input.forEachIndexed { y, line ->
            line.split("").forEachIndexed { x, char ->
                if (char == "#") {
                    galaxies.add(Point(x.toLong(), y.toLong()))
                }
            }
        }

        return galaxies
    }

    fun getGalaxyPairs(galaxies: Set<Point>): List<Pair<Point, Point>> {
        return galaxies.flatMapIndexed { index: Int, left: Point ->
            galaxies.drop(index + 1).map { right ->
                left to right
            }
        }
    }

    fun Collection<Point>.bounds(): Square {
        return Square(
            left = this.minOf { it.x },
            top = this.minOf { it.y },
            right = this.maxOf { it.x },
            bottom = this.maxOf { it.y },
        )
    }

    fun getShortestPathSum(input: List<String>, multiplier: Int): Long {
        val galaxies = getGalaxies(input)
        val galaxyPairs = getGalaxyPairs(galaxies)
        val bounds = galaxies.bounds()
        val horizontalSpaces = (bounds.left..bounds.right)
            .filter { horizontal -> galaxies.none { galaxy ->
                galaxy.x == horizontal
            }
        }
        val verticalSpaces = (bounds.top..bounds.bottom)
            .filter { vertical -> galaxies.none { galaxy ->
                galaxy.y == vertical
            }
        }

        return galaxyPairs.sumOf { (left, right) ->
            val horizontalDistance = (minOf(left.x, right.x)..maxOf(left.x, right.x))
                .count { it in horizontalSpaces } * (multiplier - 1)
            val verticalDistance = (minOf(left.y, right.y)..maxOf(left.y, right.y))
                .count { it in verticalSpaces } * (multiplier - 1)
            left.manhattanDistance(right) + horizontalDistance + verticalDistance
        }
    }

    val input = readInput("Day11")
    println(getShortestPathSum(input, 2))
    println(getShortestPathSum(input, 1000000))
}

