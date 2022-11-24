package com.staricka.adventofcode2022

interface Day {
  val id: Int

  fun part1(input: String)
  fun part2(input: String)

  fun run(part: DayPart = DayPart.BOTH) {
    val input = this::class.java.getResource("/day$id.txt")?.readText()
    if (input == null) {
      println("Problem reading input!")
      return
    }

    when(part) {
      DayPart.PART1 -> part1(input)
      DayPart.PART2 -> part2(input)
      DayPart.BOTH -> {
        part1(input)
        part2(input)
      }
    }
  }
}

enum class DayPart {
  PART1, PART2, BOTH
}
