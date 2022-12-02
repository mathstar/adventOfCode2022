package com.staricka.adventofcode2022

interface Day {
  val id: Int

  fun part1(input: String): Any?
  fun part2(input: String): Any?

  fun run(part: DayPart = DayPart.BOTH) {
    val input = this::class.java.getResource("/day$id.txt")?.readText()
    if (input == null) {
      println("Problem reading input!")
      return
    }

    when(part) {
      DayPart.PART1 -> println(part1(input))
      DayPart.PART2 -> println(part2(input))
      DayPart.BOTH -> {
        println(part1(input))
        println(part2(input))
      }
    }
  }
}

enum class DayPart {
  PART1, PART2, BOTH
}
