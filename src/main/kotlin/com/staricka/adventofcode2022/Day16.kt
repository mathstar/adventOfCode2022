package com.staricka.adventofcode2022

import kotlin.math.max

class Day16 : Day {
  override val id = 16

  val regex = Regex("""Valve ([A-Z]+) has flow rate=([0-9]+); tunnels? leads? to valves? ([A-Z]+(, [A-Z]+)*)""")
  data class Valve(val id: String, val flowRate: Int) {
//    companion object {
//      val regex = Regex("""Valve ([A-Z]+) has flow rate=([0-9]+); tunnels? leads? to valves? ([A-Z]+(, [A-Z]+)*)""")
//
//      fun String.parseValve(): Valve {
//        val match = regex.matchEntire(this)
//        return Valve(
//          match!!.groups[1]!!.value,
//          match.groups[2]!!.value.toInt()
//        )
//      }
//    }
  }

  fun parseInput(input: String): Pair<Map<String, Valve>, Map<String, List<String>>> {
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

  class State(
    val valves: Map<String, Valve>,
    val valveState: Map<String, Boolean>,
    val location: String,
    val time: Int,
    val pressureReleased: Int
  ) {
    constructor(valves: Map<String, Valve>) : this(
      valves,
      valves.keys.associateWith { false },
      "AA",
      0,
      0
    )

    fun flow(): Int = valveState.filter { it.value }.map { valves[it.key]!!.flowRate }.sum()

    fun canTurnValve(): Boolean = !valveState[location]!!

    fun destinations(adjacencies: Map<String, List<String>>): List<String> = adjacencies[location]!!

    fun turnValve(): State = State(
      valves,
      HashMap(valveState).also { it[location] = true },
      location,
      time + 1,
      pressureReleased + flow()
    )

    fun move(location: String, cost: Int = 1): State = State(
      valves,
      valveState,
      location,
      time + cost,
      pressureReleased + flow() * cost
    )
  }

  fun potentialGain(currentTime: Int, moveCost: Int, flowRate: Int): Int =
    max(0, (30 - (currentTime + moveCost + 1))) * flowRate

  fun bestMove(state: State, distances: Map<String, Map<String, Int>>): State? =
    state.valveState.filter { (_, open) -> !open }
      .map { (dest, _) ->  Pair(dest, potentialGain(
        state.time,
        distances[state.location]!![dest]!!,
        state.valves[dest]!!.flowRate
      ))}
      .maxByOrNull { it.second }
      ?.let {
        state.move(it.first).turnValve()
      }

  fun distances(tunnels: Map<String, List<String>>): Map<String, Map<String, Int>> {
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

  fun permutations(input: List<String>): List<List<String>> {
    if (input.isEmpty()) return listOf(emptyList())

    return input.flatMap { i ->
      permutations(input.filter { j -> j != i }).map {l ->
        listOf(i) + l
      }
    }
  }

  fun pressureReleased(
    permutation: List<String>,
    valves: Map<String, Valve>,
    distances: Map<String, Map<String, Int>>
  ): Int {
    var pressureReleased = 0
    var flow = 0
    var time = 0
    var location = "AA"

    for (dest in permutation) {
      val cost = distances[location]!![dest]!!
      if (cost < 30 - time) {
        time += cost + 1
        pressureReleased += flow * (cost + 1)
        flow += valves[dest]!!.flowRate
        location = dest
      }
    }
    pressureReleased += flow * (30 - time)
    return pressureReleased
  }

  fun maxPressure(
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

  override fun part1(input: String): Any? {
    val (valves, tunnels) = parseInput(input)
    val distances = distances(tunnels)

//    input.lines().forEach {
//      val v = it.split(" ")[1]
//      val t = tunnels[v]!!
//      val p = t.size > 1
//      println(
//        "Valve $v has flow rate=${valves[v]!!.flowRate}; tunnel${if (p) "s" else ""}"
//        + " lead${if (p) "" else "s"} to valve${if (p) "s" else ""} ${t.joinToString(", ")}"
//      )
//    }

    return maxPressure(
      valves.filter { (_, v) -> v.flowRate > 0 }.keys.toList(),
      valves,
      distances,
      "AA",
      0,
      0,
      0
    )

//    val permutations = valves.filter { (_, v) -> v.flowRate != 0 }
//      .map { it.key }
//      .let { permutations(it) }
//    return permutations.map { pressureReleased(it, valves, distances) }.max()

    var maxReleased = 0
    val states = ArrayDeque<State>()
    states.add(State(valves))

    while (states.isNotEmpty()) {
      val state = states.removeFirst()
      if (state.time > 30) continue
      maxReleased = max(maxReleased, state.move(state.location, 30 - state.time).pressureReleased)

      if (state.time == 30) continue

      state.valveState.filter { (_, open) -> !open }
        .filter { (v, _) -> valves[v]!!.flowRate != 0 }
        .forEach { (v, _) ->
          states.add(state.move(v, distances[state.location]!![v]!!).turnValve())
        }
    }

    return maxReleased
//    var state = State(valves)
//    while (state.time < 30) {
//      state = bestMove(state, distances) ?: state.move(state.location)
//    }
//    return state.pressureReleased
  }

  override fun part2(input: String): Any? {
    TODO("Not yet implemented")
  }
}