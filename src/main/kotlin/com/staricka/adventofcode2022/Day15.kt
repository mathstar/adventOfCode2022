package com.staricka.adventofcode2022

import com.staricka.adventofcode2022.Day15.Sensor.Companion.toSensor
import kotlin.math.abs

class Day15(
  private val part1Row: Int = 2000000,
  private val part2Limit: Int = 4000000
) : Day {
  override val id = 15

  enum class Tile {
    AIR, SENSOR, BEACON
  }

  data class Sensor(
    val x: Int,
    val y: Int,
    val beaconX: Int,
    val beaconY: Int
  ) {
    val distanceToBeacon = abs(x - beaconX) + abs(y - beaconY)

    fun ringPastExclusion(limit: Int): Set<Pair<Int, Int>> {
      val ring = HashSet<Pair<Int, Int>>()
      for(ringX in (x - distanceToBeacon - 1)..(x + distanceToBeacon + 1)) {
        if (ringX < 0 || ringX > limit) continue
        val yDist = (distanceToBeacon + 1) - abs(x - ringX)
        if (y + yDist in 0..limit) {
          ring.add(Pair(ringX, y + yDist))
        }
        if (y - yDist in 0..limit) {
          ring.add(Pair(ringX, y - yDist))
        }
      }
      return ring
    }

    companion object {
      private val REGEX = Regex("""Sensor at x=(-?[0-9]+), y=(-?[0-9]+): closest beacon is at x=(-?[0-9]+), y=(-?[0-9]+)""")

      fun String.toSensor() : Sensor {
        val match = REGEX.matchEntire(this)
        return Sensor(
          match!!.groups[1]!!.value.toInt(),
          match.groups[2]!!.value.toInt(),
          match.groups[3]!!.value.toInt(),
          match.groups[4]!!.value.toInt()
        )
      }
    }
  }

  class Grid {
    val map = HashMap<Int, HashMap<Int, Tile>>()
    private val sensors = ArrayList<Sensor>()

    fun get(x: Int, y: Int): Tile = map[x]?.get(y) ?: Tile.AIR

    fun put(x: Int, y: Int, tile: Tile) {
      map.computeIfAbsent(x) {HashMap()}[y] = tile
    }

    fun manhattanDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int = abs(x1 - x2) + abs(y1 - y2)

    fun putSensor(sensor: Sensor) {
      put(sensor.x, sensor.y, Tile.SENSOR)
      put(sensor.beaconX, sensor.beaconY, Tile.BEACON)
      sensors.add(sensor)
    }

    fun excludedTilesInCol(y: Int): Set<Pair<Int, Int>> {
      val excluded = HashSet<Pair<Int, Int>>()

      for (sensor in sensors) {
        val x = sensor.x
        var dist = 0
        while (manhattanDistance(x + dist, y, sensor.x, sensor.y) <= sensor.distanceToBeacon) {
          if (get(x + dist, y) != Tile.BEACON) {
            excluded.add(Pair(x + dist, y))
          }
          if (get(x - dist, y) != Tile.BEACON) {
            excluded.add(Pair(x - dist, y))
          }
          dist++
        }
      }
      return excluded
    }

    private fun notExcluded(x: Int, y: Int): Boolean {
      for (sensor in sensors) {
        if (manhattanDistance(sensor.x, sensor.y, x, y) <= sensor.distanceToBeacon) {
          return false
        }
      }
      return true
    }

    fun findDistress(limit: Int): Pair<Int, Int> {
      // check ring of points just past sensor range for each sensor
      // start with sensor with lowest range for optimization
      sensors.sortedBy { it.distanceToBeacon }
        .forEach {
          val ring = it.ringPastExclusion(limit)
          ring.filter { point -> notExcluded(point.first, point.second) }
            .forEach{ point -> return point }
        }
      throw Exception("Not found")
    }
  }

  override fun part1(input: String): Any {
    val grid = Grid()
    input.lines().map { it.toSensor() }.forEach { grid.putSensor(it) }

    return grid.excludedTilesInCol(part1Row).count()
  }

  override fun part2(input: String): Any {
    val grid = Grid()
    input.lines().map { it.toSensor() }.forEach { grid.putSensor(it) }

    val distress = grid.findDistress(part2Limit)
    return distress.first.toLong() * 4000000L + distress.second.toLong()
  }
}