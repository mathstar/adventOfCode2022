package com.staricka.adventofcode2022

import com.staricka.adventofcode2022.Day24.Zone.Companion.parseZone

class Day24 : Day {
  override val id = 24

  enum class Direction(val vec: Pair<Int, Int>) {
    UP(Pair(-1, 0)), RIGHT(Pair(0, 1)), DOWN(Pair(1,0)), LEFT(Pair(0,-1));

    fun step(i: Int, j: Int): Pair<Int, Int> = step(Pair(i,j))
    fun step(p: Pair<Int, Int>): Pair<Int, Int> =
      Pair(vec.first + p.first, vec.second + p.second)
  }

  class Zone(
    val width: Int,
    val height: Int,
    val blizzards: Map<Int, Map<Int, List<Direction>>>,
    val playerPositions: Set<Pair<Int, Int>> = setOf(Pair(0,1)),
    val goal: Pair<Int, Int> = Pair(height - 1, width - 2)
  ) {
    val done = playerPositions.contains(goal)

    fun step(): Zone {
      val steppedBlizzards = HashMap<Int, HashMap<Int, ArrayList<Direction>>>()
      for ((i, m) in blizzards) {
        for ((j, dl) in m) {
          for (d in dl) {
            var (si, sj) = d.step(i, j)
            if (si == 0) si = height - 2
            if (sj == 0) sj = width - 2
            if (si == height - 1) si = 1
            if (sj == width - 1) sj = 1

            steppedBlizzards.computeIfAbsent(si) { HashMap() }.computeIfAbsent(sj) { ArrayList() }.add(d)
          }
        }
      }

      val steppedPlayer = playerPositions.flatMap {
        listOf(
          it,
          Direction.UP.step(it),
          Direction.RIGHT.step(it),
          Direction.DOWN.step(it),
          Direction.LEFT.step(it)
        )
      }.filter { (i, j) ->
        (i == 0 && j == 1) ||
            (i == height - 1 && j == width - 2) ||
            i > 0 && i < height - 1 && j > 0 && j < width - 1 && steppedBlizzards[i]?.get(j) == null
      }.toSet()

      return Zone(width, height, steppedBlizzards, steppedPlayer, goal)
    }

    companion object {
      fun String.parseZone() : Zone {
        val width = this.lines().first().length
        val height = this.lines().size
        val blizzards = HashMap<Int, HashMap<Int, List<Direction>>>()
        for ((i, line) in this.lines().withIndex()) {
          for ((j, c) in line.withIndex()) {
            val d = when(c) {
              '^' -> Direction.UP
              '>' -> Direction.RIGHT
              'v' -> Direction.DOWN
              '<' -> Direction.LEFT
              else -> null
            }
            if (d != null)
              blizzards.computeIfAbsent(i) {HashMap()}[j] = listOf(d)
          }
        }
        return Zone(width, height, blizzards)
      }
    }
  }

  override fun part1(input: String): Any {
    var zone = input.parseZone()

    var steps = 0
    while (!zone.done) {
      steps++
      zone = zone.step()
    }
    return steps
  }

  override fun part2(input: String): Any {
    var zone = input.parseZone()

    var steps = 0
    while (!zone.done) {
      steps++
      zone = zone.step()
    }
    zone = Zone(
      zone.width,
      zone.height,
      zone.blizzards,
      setOf(zone.goal),
      Pair(0, 1)
    )
    while (!zone.done) {
      steps++
      zone = zone.step()
    }
    zone = Zone(
      zone.width,
      zone.height,
      zone.blizzards,
      setOf(zone.goal),
      Pair(zone.height - 1, zone.width - 2)
    )
    while (!zone.done) {
      steps++
      zone = zone.step()
    }
    return steps
  }
}