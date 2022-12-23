package com.staricka.adventofcode2022

import kotlin.math.max
import kotlin.math.min

class Day23 : Day {
  override val id = 23

  enum class Direction(private val vec: Pair<Int, Int>) {
    N(Pair(-1,0)),
    NE(Pair(-1,1)),
    E(Pair(0,1)),
    SE(Pair(1,1)),
    S(Pair(1,0)),
    SW(Pair(1,-1)),
    W(Pair(0, -1)),
    NW(Pair(-1,-1));

    fun move(initial: Pair<Int, Int>): Pair<Int, Int> = Pair(initial.first + vec.first, initial.second + vec.second)
  }

  enum class Rule(private val checkDirections: List<Direction>, private val stepDirection: Direction) {
    NORTH(listOf(Direction.N, Direction.NE, Direction.NW), Direction.N),
    SOUTH(listOf(Direction.S, Direction.SE, Direction.SW), Direction.S),
    WEST(listOf(Direction.W, Direction.NW, Direction.SW), Direction.W),
    EAST(listOf(Direction.E, Direction.NE, Direction.SE), Direction.E);

    fun proposeStep(elf: Pair<Int, Int>, grid: Grid): Pair<Int, Int>? {
      return if (checkDirections.all { grid.get(it.move(elf)) == Tile.AIR })
        stepDirection.move(elf)
      else
        null
    }
  }

  enum class Tile {
    AIR, ELF
  }

  class Grid(
    val elves: List<Pair<Int, Int>>,
    private val rules: ArrayDeque<Rule> = Rule.values().toCollection(ArrayDeque())
  ) {
    private val tiles = HashMap<Int, HashMap<Int, Tile>>()
    var minX: Int
    var maxX: Int
    var minY: Int
    var maxY: Int

    init {
      var minX : Int? = null
      var maxX : Int? = null
      var minY : Int? = null
      var maxY : Int? = null
      for (elf in elves) {
        tiles.computeIfAbsent(elf.first){ HashMap() }[elf.second] = Tile.ELF
        minX = minX?.let { min(it, elf.first) } ?: elf.first
        maxX = maxX?.let { max(it, elf.first) } ?: elf.first
        minY = minY?.let { min(it, elf.second) } ?: elf.second
        maxY = maxY?.let { max(it, elf.second) } ?: elf.second
      }
      this.minX = minX!!
      this.maxX = maxX!!
      this.minY = minY!!
      this.maxY = maxY!!
    }

    // debug function
    @Suppress("unused")
    fun render(): String =
      (minX..maxX).map { x ->
        (minY..maxY).map { y ->
          when (get(x, y)) {
            Tile.AIR -> '.'
            Tile.ELF -> '#'
          }
        }.joinToString("")
      }.joinToString("\n")

    fun get(coord: Pair<Int, Int>) = get(coord.first, coord.second)
    fun get(x: Int, y: Int): Tile = tiles[x]?.get(y) ?: Tile.AIR

    fun step(): Grid {
      val proposals = elves.associateWith { elf ->
        if (Direction.values().all { get(it.move(elf)) == Tile.AIR }) {
          elf
        } else {
          rules.firstNotNullOfOrNull { rule ->
            rule.proposeStep(elf, this)
          } ?: elf
        }
      }

      val encountered = HashSet<Pair<Int, Int>>()
      val duplicates = HashSet<Pair<Int, Int>>()
      for (p in proposals.values) {
        if (!encountered.add(p)) duplicates.add(p)
      }

      val steppedElves = proposals.map { (elf, step) ->
        if (!duplicates.contains(step)) step else elf
      }

      rules.addLast(rules.removeFirst())
      return Grid(steppedElves, rules)
    }
  }

  private fun String.parseInput(): Grid {
    val elves = this.lines().withIndex().flatMap { (x, line) ->
      line.withIndex().filter { it.value == '#' }.map { (y, _) ->
        Pair(x, y)
      }
    }
    return Grid(elves)
  }

  override fun part1(input: String): Any {
    var grid = input.parseInput()

    for (i in 1..10) {
      grid = grid.step()
    }

    var air = 0
    for (x in grid.minX..grid.maxX) {
      for (y in grid.minY..grid.maxY) {
        if (grid.get(x, y) == Tile.AIR) air++
      }
    }
    return air
  }

  override fun part2(input: String): Any {
    var grid = input.parseInput()

    var i = 0
    while (true) {
      i++
      val newGrid = grid.step()
      if (newGrid.elves == grid.elves) return i
      grid = newGrid
    }
  }
}