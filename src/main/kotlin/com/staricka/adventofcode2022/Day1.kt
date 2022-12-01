package com.staricka.adventofcode2022

import java.util.PriorityQueue
import kotlin.math.max

class Day1 : Day {
  override val id = 1

  override fun part1(input: String) {
    var max = 0
    var sum = 0
    for (line in input.lines()) {
      if (line.isBlank()) {
        max = max(sum, max)
        sum = 0
      } else {
        sum += line.toInt()
      }
    }
    max = max(sum, max)
    println(max)
  }

  override fun part2(input: String) {
    val max = PriorityQueue<Int>(Comparator.reverseOrder())
    var sum = 0
    for (line in input.lines()) {
      if (line.isBlank()) {
        max.add(sum)
        sum = 0
      } else {
        sum += line.toInt()
      }
    }
    max.add(sum)
    println("${max.poll() + max.poll() + max.poll()}")
  }
}