package com.staricka.adventofcode2022

class Day3 : Day {
  override val id = 3

  private fun Char.itemToPriority() : Int =
    if (this.isLowerCase()) {
      this.minus('a') + 1
    } else {
      this.minus('A') + 27
    }

  override fun part1(input: String): Any =
    input.lines().sumOf {
      it.substring(0, it.length / 2).toSet().intersect(it.substring(it.length / 2).toSet()).first().itemToPriority()
    }

  override fun part2(input: String): Any {
    val lines = input.lines()
    var sum = 0
    var i = 0
    while (i < lines.size) {
      sum += lines[i++].toSet().intersect(lines[i++].toSet()).intersect(lines[i++].toSet()).first().itemToPriority()
    }
    return sum
  }
}
