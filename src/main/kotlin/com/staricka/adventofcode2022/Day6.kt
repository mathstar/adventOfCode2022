package com.staricka.adventofcode2022

class Day6 : Day {
  override val id = 6

  private fun String.findMarker(size: Int): Int {
    val buffer = ArrayDeque<Char>()
    for ((i,c) in this.withIndex()) {
      buffer.add(c)
      while (buffer.size > size) buffer.removeFirst()
      if (buffer.size == size && buffer.toSet().size == size) {
        return i + 1
      }
    }
    throw Exception("marker not found")
  }

  override fun part1(input: String): Any = input.findMarker(4)

  override fun part2(input: String): Any = input.findMarker(14)
}