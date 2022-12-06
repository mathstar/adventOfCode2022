package com.staricka.adventofcode2022

class Day6 : Day {
  override val id = 6

  override fun part1(input: String): Any {
    val buffer = ArrayDeque<Char>()
    for ((i,c) in input.withIndex()) {
      buffer.add(c)
      while (buffer.size > 4) buffer.removeFirst()
      if (buffer.size == 4 && buffer.toSet().size == 4) {
        return i + 1
      }
    }
    throw Exception("packet not found")
  }

  override fun part2(input: String): Any {
    val buffer = ArrayDeque<Char>()
    for ((i,c) in input.withIndex()) {
      buffer.add(c)
      while (buffer.size > 14) buffer.removeFirst()
      if (buffer.size == 14 && buffer.toSet().size == 14) {
        return i + 1
      }
    }
    throw Exception("message not found")
  }
}