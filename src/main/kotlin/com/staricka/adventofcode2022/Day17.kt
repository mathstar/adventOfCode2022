package com.staricka.adventofcode2022

import kotlin.math.max
import kotlin.math.min
import kotlin.system.exitProcess

class Day17 : Day {
  override val id = 17

  val rockPatterns = listOf(
    RockPattern("####"),
    RockPattern("""
      .#.
      ###
      .#.
    """.trimIndent()),
    RockPattern("""
      ..#
      ..#
      ###
    """.trimIndent()),
    RockPattern("""
      #
      #
      #
      #
    """.trimIndent()),
    RockPattern("""
      ##
      ##
    """.trimIndent())
  )

  class RockPattern(pattern: String) {
    val offsets = ArrayList<Pair<Long, Long>>()

    init {
      val height = pattern.lines().size.toLong()
      for ((i, line) in pattern.lines().withIndex()) {
        for ((j, c) in line.withIndex()) {
          if (c == '#') {
            offsets.add(Pair(j.toLong(), height - i.toLong() - 1L))
          }
        }
      }
    }
  }

  enum class Tile {
    ROCK, FALLING_ROCK, AIR
  }

  class Grid {
    val tiles = HashMap<Long, HashMap<Long, Tile>>()
    var maxRockHeight = -1L

    fun render() {
      for (y in (0L..maxRockHeight).reversed()) {
        print("|")
        for (x in 0L..6L) {
          print(when (get(x, y)) {
            Tile.AIR -> '.'
            Tile.FALLING_ROCK -> '@'
            Tile.ROCK -> '#'
          })
        }
        println("|")
      }
      println("---------")
      println()
    }

    fun get(x: Long, y: Long): Tile = tiles[x]?.get(y) ?: Tile.AIR

    fun put(x: Long, y: Long, t: Tile) {
      tiles.computeIfAbsent(x) { HashMap() }[y] = t
    }

    fun clear(x: Long, y: Long) {
      tiles.computeIfAbsent(x) { HashMap() }.remove(y)
    }

    fun placeRock(rockPattern: RockPattern) {
      val originX = 2L
      val originY = maxRockHeight + 4L

      for ((xOffset, yOffset) in rockPattern.offsets) {
        put(originX + xOffset, originY + yOffset, Tile.FALLING_ROCK)
      }
    }

    fun step(direction: Pair<Long, Long>): Boolean {
      val (xShift, yShift) = direction

      val fallingTiles = tiles.flatMap { (x, m) ->
        m.filter { (_, t) ->
          t == Tile.FALLING_ROCK
        }.map { (y, _) ->
          Pair(x,y)
        }
      }
      if (fallingTiles.isEmpty()) return false

      var canMove = true
      for ((x, y) in fallingTiles) {
        if (x + xShift > 6L || x + xShift < 0L) {
          canMove = false
          break
        }
        if (y + yShift < 0L) {
          canMove = false
          break
        }
        if (get(x + xShift, y + yShift) !in listOf(Tile.AIR, Tile.FALLING_ROCK)) {
          canMove = false
          break
        }
      }
      if (!canMove) {
        return false
      }

      for ((x, y) in fallingTiles) {
        clear(x, y)
      }
      for ((x, y) in fallingTiles) {
        put(x + xShift, y + yShift, Tile.FALLING_ROCK)
      }
      return true
    }

    fun stopFalling(): Long {
      var minX = 10L
      val fallingTiles = tiles.flatMap { (x, m) ->
        m.filter { (_, t) ->
          t == Tile.FALLING_ROCK
        }.map { (y, _) ->
          Pair(x,y)
        }
      }
      maxRockHeight = max(maxRockHeight, fallingTiles.maxOfOrNull { it.second } ?: maxRockHeight)
      for ((x, y) in fallingTiles) {
        minX = min(minX, x)
        put(x, y, Tile.ROCK)
      }
      return minX
    }
  }

  fun parseDirections(input: String): List<Pair<Long, Long>> =
    input.map {
      when (it) {
        '<' -> Pair(-1L, 0L)
        '>' -> Pair(1L, 0L)
        else -> null
      }
    }.filterNotNull()

  val down = Pair(0L, -1L)

  override fun part1(input: String): Any? {
    val directions = parseDirections(input)
    val grid = Grid()
    var directionIndex = 0

    for (i in 0 until 2022) {
      val rockPattern = rockPatterns[i % rockPatterns.size]
      grid.placeRock(rockPattern)

      while (true) {
        grid.step(directions[directionIndex++])
        directionIndex %= directions.size

        if (!grid.step(down)) {
          grid.stopFalling()
          //grid.render()
          break
        }
      }
    }

    return grid.maxRockHeight + 1
  }

  override fun part2(input: String): Any? {
    val directions = parseDirections(input)
    val grid = Grid()
    var directionIndex = 0

    val maxHeightAtStep = HashMap<Long, Long>()
    val cycleCheck = HashMap<Triple<Long, Long, Long>, Long>()

    for (i in 0 until 1000000000000) {
      val rockPattern = rockPatterns[(i % rockPatterns.size).toInt()]
      grid.placeRock(rockPattern)

      while (true) {
        grid.step(directions[directionIndex++])
        directionIndex %= directions.size

        if (!grid.step(down)) {
          val minX = grid.stopFalling()
          maxHeightAtStep[i] = grid.maxRockHeight

          val cycleKey = Triple((i % rockPatterns.size), directionIndex - 1L, minX)
          if (cycleCheck.containsKey(cycleKey)) {

            val cycleStart = cycleCheck[cycleKey]!!
            val cycleEnd = i
            val cycleLength = cycleEnd - cycleStart
            val cycleHeight = grid.maxRockHeight - maxHeightAtStep[cycleStart]!!

            val remainingSteps = 1000000000000L - cycleStart - 1
            val cycles = remainingSteps / cycleLength
            val remainderSteps = remainingSteps % cycleLength
            val remainderHeight = maxHeightAtStep[cycleStart + remainderSteps]!! - maxHeightAtStep[cycleStart]!!

            return maxHeightAtStep[cycleStart]!! + cycleHeight * cycles + remainderHeight + 1
          }
          cycleCheck[cycleKey] = i

          break
        }
      }
    }

    return grid.maxRockHeight + 1
  }
}