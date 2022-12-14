package com.staricka.adventofcode2022

import kotlin.math.max
import kotlin.math.min

class Day14 : Day {
  override val id = 14

  enum class Tile {
    AIR, ROCK, SAND
  }

  class Grid {
    val map = HashMap<Int, MutableMap<Int, Tile>>()
    private var maxRockY = Int.MIN_VALUE

    fun get(x: Int, y: Int): Tile = map[x]?.get(y) ?: Tile.AIR

    fun put(x: Int, y: Int, tile: Tile) {
      map.computeIfAbsent(x) {HashMap()}[y] = tile
    }

    fun putRockSegment(a: Pair<Int, Int>, b: Pair<Int, Int>) {
      if (a.first == b.first) {
        val start = min(a.second, b.second)
        val end = max(a.second, b.second)
        maxRockY = max(maxRockY, end)

        for (i in start..end) {
          put(a.first, i, Tile.ROCK)
        }
      } else {
        val start = min(a.first, b.first)
        val end = max(a.first, b.first)
        maxRockY = max(maxRockY, a.second)

        for (i in start..end) {
          put(i, a.second, Tile.ROCK)
        }
      }
    }

    // true if drop terminates
    fun dropSand(dropX: Int, dropY: Int): Boolean {
      var x = dropX
      var y = dropY

      while (y < maxRockY) {
        when (Tile.AIR) {
          get(x, y + 1) -> {
            y++
          }
          get(x - 1, y + 1) -> {
            x--
            y++
          }
          get(x + 1, y + 1) -> {
            x++
            y++
          }
          else -> {
            put(x, y, Tile.SAND)
            return true
          }
        }
      }
      return false
    }

    // true if drop terminates
    fun dropSandPart2(dropX: Int, dropY: Int): Boolean {
      var x = dropX
      var y = dropY

      while (y < maxRockY + 1) {
        when (Tile.AIR) {
          get(x, y + 1) -> {
            y++
          }
          get(x - 1, y + 1) -> {
            x--
            y++
          }
          get(x + 1, y + 1) -> {
            x++
            y++
          }
          else -> {
            put(x, y, Tile.SAND)
            return x != 500 || y != 0
          }
        }
      }
      put(x, y, Tile.SAND)
      return true
    }
  }

  private fun generateGrid(input: String): Grid {
    val grid = Grid()
    input.lines().forEach { line ->
      val split = line.split(" -> ")
      for (i in 1 until split.size) {
        val a = split[i - 1].split(",")
        val b = split[i].split(",")
        grid.putRockSegment(Pair(a[0].toInt(), a[1].toInt()), Pair(b[0].toInt(), b[1].toInt()))
      }
    }
    return grid
  }

  override fun part1(input: String): Any {
    val grid = generateGrid(input)
    var sand = 0
    while (grid.dropSand(500, 0)) {
      sand++
    }
    return sand
  }

  override fun part2(input: String): Any {
    val grid = generateGrid(input)
    var sand = 0
    while (grid.dropSandPart2(500, 0)) {
      sand++
    }
    return sand + 1
  }
}