fun main() {
    val tiles = readInput("Day10").map { it.toByteArray() }
    val start = findStart(tiles)
    val (moves, enclosedTiles) = navigateMaze(tiles, start)

    println(moves / 2 )
    println(enclosedTiles)
}

fun findStart(tiles: List<ByteArray>): Pair<Int, Int> {
    for (row in tiles.indices) {
        val column = tiles[row].indexOf('S'.code.toByte())
        if (column != -1) return Pair(column, row)
    }

    return Pair(0, 0)
}

fun navigateMaze(tiles: List<ByteArray>, start: Pair<Int, Int>): Pair<Int, Int> {
    var moves = 0
    var position = start
    var direction = getInitialDirection(tiles, position)
    val loop = HashSet<Pair<Int, Int>>()
    val secondLoop = HashSet<Pair<Int, Int>>()

    do {
        updateLoopSets(loop, secondLoop, position)
        position = move(position, direction)
        direction = getNewDirection(tiles, position, direction)
        moves++
    } while (position != start)

    extendLoops(tiles, secondLoop)
    val enclosedTiles = countEnclosedTiles(tiles, loop, secondLoop)

    return Pair(moves, enclosedTiles)
}

fun tile(tiles: List<ByteArray>, column: Int, row: Int): Char {
    if (row < 0 || row >= tiles.size || column < 0 || column >= tiles[row].size) return ' '
    return tiles[row][column].toInt().toChar()
}

fun getInitialDirection(tiles: List<ByteArray>, position: Pair<Int, Int>): Pair<Int, Int> {
    val UP = Pair(0, -1)
    val DOWN = Pair(0, 1)
    val LEFT = Pair(-1, 0)
    val RIGHT = Pair(1, 0)

    return when {
        "|F7".contains(tile(tiles, position.first, position.second - 1)) -> UP
        "|JL".contains(tile(tiles, position.first, position.second + 1)) -> DOWN
        "-LF".contains(tile(tiles, position.first - 1, position.second)) -> LEFT
        "-J7".contains(tile(tiles, position.first + 1, position.second)) -> RIGHT
        else -> RIGHT
    }
}

fun updateLoopSets(loop: MutableSet<Pair<Int, Int>>, secondLoop: MutableSet<Pair<Int, Int>>, position: Pair<Int, Int>) {
    loop.add(position)
    secondLoop.add(Pair(position.first * 2, position.second * 2))
}

fun move(position: Pair<Int, Int>, direction: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(position.first + direction.first, position.second + direction.second)
}

fun getNewDirection(tiles: List<ByteArray>, position: Pair<Int, Int>, currentDirection: Pair<Int, Int>): Pair<Int, Int> {
    val UP = Pair(0, -1)
    val DOWN = Pair(0, 1)
    val LEFT = Pair(-1, 0)
    val RIGHT = Pair(1, 0)

    return when (tile(tiles, position.first, position.second)) {
        'L' -> if (currentDirection == LEFT) UP else if (currentDirection == DOWN) RIGHT else currentDirection
        'J' -> if (currentDirection == RIGHT) UP else if (currentDirection == DOWN) LEFT else currentDirection
        '7' -> if (currentDirection == RIGHT) DOWN else if (currentDirection == UP) LEFT else currentDirection
        'F' -> if (currentDirection == LEFT) DOWN else if (currentDirection == UP) RIGHT else currentDirection
        else -> currentDirection
    }
}

fun extendLoops(tiles: List<ByteArray>, secondLoop: MutableSet<Pair<Int, Int>>) {
    for (row in 0 until (tiles.size * 2)) {
        for (column in 0 until (tiles[0].size * 2)) {
            if (extendSecondLoop(tiles, secondLoop, column, row)) {
                secondLoop.add(Pair(column, row))
            }
        }
    }
}

fun extendSecondLoop(tiles: List<ByteArray>, secondLoop: Set<Pair<Int, Int>>, column: Int, row: Int): Boolean {
    return (secondLoop.contains(Pair(column + 1, row)) && secondLoop.contains(Pair(column - 1, row)) &&
            "L-FS".contains(tile(tiles, (column - 1) / 2, row / 2)) && "J-7S".contains(tile(tiles, (column + 1) / 2, row / 2))) ||
            (secondLoop.contains(Pair(column, row - 1)) && secondLoop.contains(Pair(column, row + 1)) &&
                    "7|FS".contains(tile(tiles, column / 2, (row - 1) / 2)) && "J|LS".contains(tile(tiles, column / 2, (row + 1) / 2)))
}

fun countEnclosedTiles(tiles: List<ByteArray>, loop: Set<Pair<Int, Int>>, secondLoop: Set<Pair<Int, Int>>): Int {
    var enclosedTiles = 0
    for (row in tiles.indices) {
        for (column in tiles[row].indices) {
            if (!loop.contains(Pair(column, row)) && isEnclosed(secondLoop, Pair(column * 2, row * 2), tiles)) {
                enclosedTiles++
            }
        }
    }

    return enclosedTiles
}

fun isEnclosed(loop: Set<Pair<Int, Int>>, position: Pair<Int, Int>, tiles: List<ByteArray>): Boolean {
    val addedPositions = HashSet<Pair<Int, Int>>()
    val queue = ArrayDeque<Pair<Int, Int>>()
    queue.add(position)

    while (queue.isNotEmpty()) {
        val nextPosition = queue.removeFirst()
        if (addedPositions.contains(nextPosition)) continue
        addedPositions.add(nextPosition)
        if (!isPositionValid(nextPosition, tiles)) return false
        addNeighborsToQueue(nextPosition, loop, queue)
    }

    return true
}

fun isPositionValid(position: Pair<Int, Int>, tiles: List<ByteArray>): Boolean {
    return !(position.first < 0 || position.second < 0 || position.first >= tiles[0].size * 2 || position.second >= tiles.size * 2)
}

fun addNeighborsToQueue(position: Pair<Int, Int>, loop: Set<Pair<Int, Int>>, queue: ArrayDeque<Pair<Int, Int>>) {
    val (x, y) = position
    val neighbors = listOf(Pair(x + 1, y), Pair(x, y + 1), Pair(x - 1, y), Pair(x, y - 1))
    neighbors.filter { it !in loop }.forEach { queue.add(it) }
}