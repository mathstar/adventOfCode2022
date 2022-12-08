package com.staricka.adventofcode2022

import kotlin.math.max

class Day8 : Day {
  override val id = 8

  override fun part1(input: String): Any {
    val visible = HashSet<Pair<Int, Int>>()
    val grid = input.lines()
    for (i in grid.indices) {
      var maxHeight = -1
      for (j in grid[i].indices) {
        if (grid[i][j].digitToInt() > maxHeight) {
          visible.add(Pair(i,j))
          maxHeight = max(maxHeight, grid[i][j].digitToInt())
        }
      }
      maxHeight = -1
      for (j in grid[i].indices.reversed()) {
        if (grid[i][j].digitToInt() > maxHeight) {
          visible.add(Pair(i,j))
          maxHeight = max(maxHeight, grid[i][j].digitToInt())
        }
      }
    }

    for (j in grid[0].indices) {
      var maxHeight = -1
      for (i in grid.indices) {
        if (grid[i][j].digitToInt() > maxHeight) {
          visible.add(Pair(i,j))
          maxHeight = max(maxHeight, grid[i][j].digitToInt())
        }
      }
      maxHeight = -1
      for (i in grid.indices.reversed()) {
        if (grid[i][j].digitToInt() > maxHeight) {
          visible.add(Pair(i,j))
          maxHeight = max(maxHeight, grid[i][j].digitToInt())
        }
      }
    }

    return visible.size
  }

  private fun computeScore(grid: Array<IntArray>, x: Int, y: Int): Int {
    var aScore = 0
    for (i in (0 until x).reversed()) {
      aScore++
      if (grid[i][y] >= grid[x][y]) {
        break
      }
    }
    var bScore = 0
    for (i in (x+1)until grid.size) {
      bScore++
      if (grid[i][y] >= grid[x][y]) {
        break
      }
    }
    var cScore = 0
    for (j in (0 until y).reversed()) {
      cScore++
      if (grid[x][j] >= grid[x][y]) {
        break
      }
    }
    var dScore = 0
    for (j in (y+1) until grid[0].size) {
      dScore++
      if (grid[x][j] >= grid[x][y]) {
        break
      }
    }
    return aScore * bScore * cScore * dScore
  }

  override fun part2(input: String): Any {
    var maxScore = 0
    val grid = input.lines().map { it.map(Char::digitToInt).toIntArray() }.toTypedArray()
    for (i in grid.indices) {
      for (j in grid[i].indices) {
        maxScore = max(maxScore, computeScore(grid, i, j))
      }
    }
    return maxScore
  }
}