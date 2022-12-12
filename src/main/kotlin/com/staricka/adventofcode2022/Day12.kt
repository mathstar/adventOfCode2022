package com.staricka.adventofcode2022

class Day12 : Day {
  override val id = 12

  data class Tile(val height: Char) {
    fun canStepFrom(other: Tile): Boolean = other.height - height >= -1
    fun canStepReverse(other: Tile): Boolean = other.canStepFrom(this)
  }

  private fun shortestPath(
    start: Pair<Int, Int>, grid: List<List<Tile>>,
    stepCondition: Tile.(Tile) -> Boolean,
    endCondition: (Pair<Int, Int>, Tile) -> Boolean
  ): Int {
    val explored = Array(grid.size) { BooleanArray(grid[0].size) }
    explored[start.first][start.second] = true

    var next = listOf(start)
    var steps = 0
    while (true) {
      val nextStep = ArrayList<Pair<Int, Int>>()
      for (n in next) {
        val (i, j) = n
        if (endCondition(n, grid[i][j])) return steps
        if (i > 0 && grid[i - 1][j].stepCondition(grid[i][j]) && !explored[i-1][j]) {
          explored[i-1][j] = true
          nextStep.add(Pair(i - 1, j))
        }
        if (j > 0 && grid[i][j - 1].stepCondition(grid[i][j]) && !explored[i][j-1]) {
          explored[i][j-1] = true
          nextStep.add(Pair(i, j - 1))
        }
        if (i < grid.size - 1 && grid[i + 1][j].stepCondition(grid[i][j]) && !explored[i+1][j]) {
          explored[i+1][j] = true
          nextStep.add(Pair(i + 1, j))
        }
        if (j < grid[0].size - 1 && grid[i][j + 1].stepCondition(grid[i][j]) && !explored[i][j+1]) {
          explored[i][j+1] = true
          nextStep.add(Pair(i, j + 1))
        }
      }
      steps++
      next = nextStep
    }
  }

  override fun part1(input: String): Any {
    var start = Pair(0,0)
    var end = Pair(0,0)

    val grid = input.lines().withIndex().map { (i, line) ->
      line.withIndex().map {(j, t) ->
        when (t) {
          'S' -> {
            start = Pair(i,j)
            Tile('a')
          }
          'E' -> {
            end = Pair(i,j)
            Tile('z')
          }
          else -> Tile(t)
        }
      }
    }

    return shortestPath(start, grid, Tile::canStepFrom) {coord, _ -> coord == end}
  }

  override fun part2(input: String): Any {
    var end = Pair(0,0)

    val grid = input.lines().withIndex().map { (i, line) ->
      line.withIndex().map {(j, t) ->
        when (t) {
          'S' -> {
            Tile('a')
          }
          'E' -> {
            end = Pair(i,j)
            Tile('z')
          }
          else -> Tile(t)
        }
      }
    }

    return shortestPath(end, grid, Tile::canStepReverse) {_, tile -> tile.height == 'a'}
  }
}