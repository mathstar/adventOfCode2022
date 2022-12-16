package com.staricka.adventofcode2022

import kotlin.math.max

class Day16 : Day {
  override val id = 16

  data class Valve(val id: String, val flowRate: Int)

  private val regex = Regex("""Valve ([A-Z]+) has flow rate=([0-9]+); tunnels? leads? to valves? ([A-Z]+(, [A-Z]+)*)""")
  private fun parseInput(input: String): Pair<Map<String, Valve>, Map<String, List<String>>> {
    val valves = HashMap<String, Valve>()
    val tunnels = HashMap<String, List<String>>()

    input.lines().forEach { line ->
      val match = regex.matchEntire(line)
      val valve = Valve(
        match!!.groups[1]!!.value,
        match.groups[2]!!.value.toInt()
      )
      valves[valve.id] = valve
      tunnels[valve.id] = match.groups[3]!!.value.split(", ")
    }
    return Pair(valves, tunnels)
  }

  /**
   * Compute distance to travel from each valve to each other valve
   */
  private fun distances(tunnels: Map<String, List<String>>): Map<String, Map<String, Int>> {
    var result = HashMap<String, HashMap<String, Int>>()
    for ((k, l) in tunnels) {
      for (v in l) {
        result.computeIfAbsent(k) { HashMap() }[k] = 0
        result.computeIfAbsent(k) { HashMap() }[v] = 1
        result.computeIfAbsent(v) { HashMap() }[k] = 1
      }
    }

    var changed = true
    while (changed) {
      val nextResult = result.map { (k, v) -> k to HashMap(v) }.toMap(HashMap())
      changed = false
      for (k in tunnels.keys) {
        for (v in tunnels.keys) {
          if (!result[k]!!.containsKey(v)) {
            val minConnection = result[k]!!.filter { (intermediate, _) ->
              result[intermediate]!!.containsKey(v)
            }.map { (intermediate, iCost) ->
              iCost + result[intermediate]!![v]!!
            }.minOrNull()
            if (minConnection != null) {
              nextResult[k]!![v] = minConnection
              nextResult[v]!![k] = minConnection
              changed = true
            }
          }
        }
      }
      result = nextResult
    }

    return result
  }

  private fun maxPressure(
    toVisit: List<String>,
    valves: Map<String, Valve>,
    distances: Map<String, Map<String, Int>>,
    location: String,
    time: Int,
    flowRate: Int,
    pressureReleased: Int
  ) : Int {
    if (time > 30) {
      return -1
    }

    var max = pressureReleased + (30 - time) * flowRate
    for (v in toVisit) {
      max = max(max, maxPressure(
        toVisit.filter { j -> j != v },
        valves,
        distances,
        v,
        time + distances[location]!![v]!! + 1,
        flowRate + valves[v]!!.flowRate,
        pressureReleased + flowRate * (distances[location]!![v]!! + 1)
      ))
    }
    return max
  }

  /*
   * PART 2 Optimizations Section
   */

  // cull redundant paths
  private val pathCullingSet = HashSet<String>()
  private fun haveNotEncounteredPath(path1: String, path2: String): Boolean =
    if (path1 < path2) {
      pathCullingSet.add("$path1,$path2")
    } else {
      pathCullingSet.add("$path2,$path1")
    }

  // heuristic for whether this path could possibly improve the result
  private var globalMax = 0
  private fun canImprove(
    currentEndValue: Int,
    toVisit: List<String>,
    valves: Map<String, Valve>,
    initialTimeRemaining: Int
  ) : Boolean {
    // assume it takes two minutes to open each valve (i.e. shortest possible path)
    var result = currentEndValue
    var timeRemaining = initialTimeRemaining
    val sortedValves = toVisit.sortedBy { valves[it]!!.flowRate }.toMutableList()
    while (sortedValves.size > 0 && timeRemaining > 2) {
      timeRemaining -= 2
      result += valves[sortedValves.removeLast()]!!.flowRate * timeRemaining
      if (sortedValves.size > 0) {
        result += valves[sortedValves.removeLast()]!!.flowRate * timeRemaining
      }
    }
    return result > globalMax
  }

  private fun maxPressureTwoWorkers(
    toVisit: List<String>,
    valves: Map<String, Valve>,
    distances: Map<String, Map<String, Int>>,
    location1: String,
    worker1Transit: Pair<String, Int>?,
    worker1Path: String,
    location2: String,
    worker2Transit: Pair<String, Int>?,
    worker2Path: String,
    time: Int,
    flowRate: Int,
    pressureReleased: Int
  ) : Int {
    if (time > 26) {
      return -1
    }

    var max = pressureReleased + (26 - time) * flowRate
    if (worker1Transit != null) {
      max += max(0, 26 - worker1Transit.second - time) * valves[worker1Transit.first]!!.flowRate
    }
    if (worker2Transit != null) {
      max += max(0, 26 - worker2Transit.second - time) * valves[worker2Transit.first]!!.flowRate
    }
    val base = max

    if (worker1Transit == null && canImprove(base, toVisit, valves, 26 - time)) {
      for (v in toVisit) {
        if (haveNotEncounteredPath(worker1Path + v, worker2Path)) {
          max = max(
            max, maxPressureTwoWorkers(
              toVisit.filter { j -> j != v },
              valves,
              distances,
              v,
              Pair(v, distances[location1]!![v]!! + 1),
              worker1Path + v,
              location2,
              worker2Transit,
              worker2Path,
              time,
              flowRate,
              pressureReleased
            )
          )
        }
      }
    } else if (worker1Transit != null && (toVisit.isEmpty() || (worker2Transit != null && worker1Transit.second <= worker2Transit.second))) {
      max = max(max, maxPressureTwoWorkers(
        toVisit,
        valves,
        distances,
        location1,
        null,
        worker1Path,
        location2,
        if (worker2Transit != null) Pair(worker2Transit.first, worker2Transit.second - worker1Transit.second) else null,
        worker2Path,
        time + worker1Transit.second,
        flowRate + valves[worker1Transit.first]!!.flowRate,
        pressureReleased + flowRate * worker1Transit.second
      ))
    }

    if (worker2Transit == null && canImprove(base, toVisit, valves, 26 - time)) {
      for (v in toVisit) {
        if (haveNotEncounteredPath(worker1Path, worker2Path + v)) {
          max = max(
            max, maxPressureTwoWorkers(
              toVisit.filter { j -> j != v },
              valves,
              distances,
              location1,
              worker1Transit,
              worker1Path,
              v,
              Pair(v, distances[location2]!![v]!! + 1),
              worker2Path + v,
              time,
              flowRate,
              pressureReleased
            )
          )
        }
      }
    } else if (worker2Transit != null && (toVisit.isEmpty() || (worker1Transit != null && worker2Transit.second < worker1Transit.second))){
      max = max(max, maxPressureTwoWorkers(
        toVisit,
        valves,
        distances,
        location1,
        if (worker1Transit != null) Pair(worker1Transit.first, worker1Transit.second - worker2Transit.second) else null,
        worker1Path,
        location2,
        null,
        worker2Path,
        time + worker2Transit.second,
        flowRate + valves[worker2Transit.first]!!.flowRate,
        pressureReleased + flowRate * worker2Transit.second
      ))
    }

    globalMax = max(globalMax, max)
    return max
  }

  override fun part1(input: String): Any {
    val (valves, tunnels) = parseInput(input)
    val distances = distances(tunnels)

    return maxPressure(
      valves.filter { (_, v) -> v.flowRate > 0 }.keys.toList(),
      valves,
      distances,
      "AA",
      0,
      0,
      0
    )
  }

  override fun part2(input: String): Any {
    val (valves, tunnels) = parseInput(input)
    val distances = distances(tunnels)

    return maxPressureTwoWorkers(
      valves.filter { (_, v) -> v.flowRate > 0 }.keys.toList(),
      valves,
      distances,
      "AA",
      null,
      "",
      "AA",
      null,
      "",
      0,
      0,
      0
    )
  }
}