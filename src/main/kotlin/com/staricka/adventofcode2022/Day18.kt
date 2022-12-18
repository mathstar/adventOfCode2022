package com.staricka.adventofcode2022

import com.staricka.adventofcode2022.Day18.Cube.Companion.parseCube

class Day18 : Day {
  override val id = 18

  data class Cube(val x: Int, val y: Int, val z: Int) {
    val neighbors by lazy {
      listOf(
        Cube(x - 1, y, z),
        Cube(x + 1, y, z),
        Cube(x, y - 1, z),
        Cube(x, y + 1, z),
        Cube(x, y, z - 1),
        Cube(x, y, z + 1),
      )
    }

    companion object {
      fun String.parseCube(): Cube {
        val split = this.split(",")
        return Cube(split[0].toInt(), split[1].toInt(), split[2].toInt())
      }
    }
  }

  override fun part1(input: String): Any {
    val cubes = input.lines().map { it.parseCube() }.toHashSet()

    var exposedSides = 0
    for (cube in cubes) {
      exposedSides += cube.neighbors.count { !cubes.contains(it) }
    }

    return exposedSides
  }

  override fun part2(input: String): Any {
    val cubes = input.lines().map { it.parseCube() }.toHashSet()

    val xNegBound = cubes.minOfOrNull { it.x }!! - 1
    val xPosBound = cubes.maxOfOrNull { it.x }!! + 1
    val yNegBound = cubes.minOfOrNull { it.y }!! - 1
    val yPosBound = cubes.maxOfOrNull { it.y }!! + 1
    val zNegBound = cubes.minOfOrNull { it.z }!! - 1
    val zPosBound = cubes.maxOfOrNull { it.z }!! + 1

    val water = HashSet<Cube>()
    val flow = ArrayDeque<Cube>()
    flow.add(Cube(xNegBound, yNegBound, zNegBound))
    water.add(flow.first())

    var surface = 0
    while (flow.isNotEmpty()) {
      val w = flow.removeFirst()
      surface += w.neighbors.count { cubes.contains(it) }
      w.neighbors.filter { !cubes.contains(it) }
        .filter { !water.contains(it) }
        .filter { it.x in xNegBound..xPosBound &&
            it.y in yNegBound..yPosBound &&
            it.z in zNegBound..zPosBound}
        .forEach {
          water.add(it)
          flow.add(it)
        }
    }
    return surface
  }
}