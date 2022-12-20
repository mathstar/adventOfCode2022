package com.staricka.adventofcode2022

import com.staricka.adventofcode2022.Day19.Blueprint.Companion.parseBlueprint
import java.util.TreeMap
import kotlin.math.max
import kotlin.math.min

class Day19 : Day {
  override val id = 19

  // TODO logic is definitely incorrect here
  class ResourceCache {
    val cache = TreeMap<Int, TreeMap<Int, TreeMap<Int, TreeMap<Int, TreeMap<Int, TreeMap<Int, TreeMap<Int, TreeMap<Int, TreeMap<Int, Int>>>>>>>>>()

    fun put(time: Int, resources: Resources, result: Int) {
      cache.getMap(time)
        .getMap(resources.ore)
        .getMap(resources.clay)
        .getMap(resources.obsidian)
        .getMap(resources.geode)
        .getMap(resources.oreRobot)
        .getMap(resources.clayRobot)
        .getMap(resources.obsidianRobot)
        .put(resources.geodeRobot, result)
    }

    fun get(time: Int, resources: Resources): Int? =
      cache.ceilingEntry(time)?.value
        ?.ceilingEntry(resources.ore)?.value
        ?.ceilingEntry(resources.clay)?.value
        ?.ceilingEntry(resources.obsidian)?.value
        ?.ceilingEntry(resources.geode)?.value
        ?.ceilingEntry(resources.oreRobot)?.value
        ?.ceilingEntry(resources.clayRobot)?.value
        ?.ceilingEntry(resources.obsidianRobot)?.value
        ?.ceilingEntry(resources.geodeRobot)?.value

    companion object {
      fun <A, K, V> TreeMap<A, TreeMap<K,V>>.getMap(key: A): TreeMap<K, V> =
        this.computeIfAbsent(key) {TreeMap()}
    }
  }

  data class Resources(
    val ore: Int,
    val clay: Int,
    val obsidian: Int,
    val geode: Int,
    val oreRobot: Int,
    val clayRobot: Int,
    val obsidianRobot: Int,
    val geodeRobot: Int
  ) {
    constructor() : this(0,0,0, 0, 1,0,0,0)

    fun produce(): Resources = Resources(
      ore + oreRobot,
      clay + clayRobot,
      obsidian + obsidianRobot,
      geode + geodeRobot,
      oreRobot,
      clayRobot,
      obsidianRobot,
      geodeRobot
    )

    fun buildOreRobot(blueprint: Blueprint, count: Int = 1): Resources = Resources(
      ore - blueprint.oreOreCost * count,
      clay,
      obsidian,
      geode,
      oreRobot + count,
      clayRobot,
      obsidianRobot,
      geodeRobot
    )

    fun buildClayRobot(blueprint: Blueprint, count: Int = 1): Resources = Resources(
      ore - blueprint.clayOreCost * count,
      clay,
      obsidian,
      geode,
      oreRobot,
      clayRobot + count,
      obsidianRobot,
      geodeRobot
    )

    fun buildObsidianRobot(blueprint: Blueprint, count: Int) : Resources = Resources(
      ore - blueprint.obsidianOreCost * count,
      clay - blueprint.obsidianClayCost * count,
      obsidian,
      geode,
      oreRobot,
      clayRobot,
      obsidianRobot + count,
      geodeRobot
    )

    fun buildGeodeRobot(blueprint: Blueprint, count: Int) : Resources = Resources(
      ore - blueprint.geodeOreCost * count,
      clay,
      obsidian - blueprint.geodeObsidianCost * count,
      geode,
      oreRobot,
      clayRobot,
      obsidianRobot,
      geodeRobot + count
    )

    fun invalid() = ore < 0 || clay < 0 || obsidian < 0 || geode < 0
  }

  data class Blueprint(
    val id: Int,
    val oreOreCost: Int,
    val clayOreCost: Int,
    val obsidianOreCost: Int,
    val obsidianClayCost: Int,
    val geodeOreCost: Int,
    val geodeObsidianCost: Int
  ) {
    fun oreBuildable(resources: Resources): Int = resources.ore / oreOreCost
    fun clayBuildable(resources: Resources): Int = resources.ore / clayOreCost
    fun obsidianBuildable(resources: Resources): Int = min(resources.ore / obsidianOreCost, resources.clay / clayOreCost)
    fun geodeBuildable(resources: Resources): Int = min(resources.ore / geodeOreCost, resources.obsidian / geodeObsidianCost)

    fun greedyGeode(resources: Resources, time: Int = 24): Int {
      if (time == 0) return resources.geode
      var r = resources.produce()
      r = r.buildGeodeRobot(this, geodeBuildable(r))
      r = r.buildObsidianRobot(this, obsidianBuildable(r))
      r = r.buildClayRobot(this, clayBuildable(r))
      r = r.buildOreRobot(this, oreBuildable(r))
      return greedyGeode(r, time - 1)
    }

    val cache = ResourceCache()
    fun greatestGeodeCount(resources: Resources, time: Int = 24): Int {
      if (time == 0) return resources.geode

      var r = resources.produce()
      var max = 0

      for (ore in 0..oreBuildable(resources)) {
        r = r.buildOreRobot(this, ore)

        for (clay in 0..clayBuildable(r)) {
          r = r.buildClayRobot(this, clay)

          for (obsidian in 0..obsidianBuildable(r)) {
            r = r.buildObsidianRobot(this, obsidian)

            for (geode in 0..geodeBuildable(r)) {
              r = r.buildGeodeRobot(this, geode)

              var result = cache.get(time, r)
              if (result == null) {
                result = greatestGeodeCount(r, time - 1)
                cache.put(time, r, result)
              }
              max = max(max, greatestGeodeCount(r, time - 1))
            }
          }
        }
      }

      return max
    }

    fun qualityScore(): Int = id * greatestGeodeCount(Resources())

    companion object {
      val regex = Regex("[^0-9]*([0-9]+)[^0-9]*([0-9]+)[^0-9]*([0-9]+)[^0-9]*([0-9]+)[^0-9]*([0-9]+)[^0-9]*([0-9]+)[^0-9]*([0-9]+)[^0-9]*")

      fun String.parseBlueprint(): Blueprint {
        val match = regex.matchEntire(this)!!

        return Blueprint(
          match.groups[1]!!.value.toInt(),
          match.groups[2]!!.value.toInt(),
          match.groups[3]!!.value.toInt(),
          match.groups[4]!!.value.toInt(),
          match.groups[5]!!.value.toInt(),
          match.groups[6]!!.value.toInt(),
          match.groups[7]!!.value.toInt()
        )
      }
    }
  }

  override fun part1(input: String): Any? {
    val blueprints = input.lines().map { it.parseBlueprint() }

    return blueprints.sumOf { it.qualityScore() }
//    return blueprints.sumOf { it.id * it.greedyGeode(Resources()) }
  }

  override fun part2(input: String): Any? {
    TODO("Not yet implemented")
  }
}