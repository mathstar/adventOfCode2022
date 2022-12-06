package com.staricka.adventofcode2022

interface Day {
  val id: Int

  fun part1(input: String): Any?
  fun part2(input: String): Any?

  fun part1FromResource(): Any? = part1(resource)
  fun part2FromResource(): Any? = part2(resource)

  val resource: String
    get() = this::class.java.getResource("/day$id.txt")?.readText() ?: throw Exception("Problem reading input!")

  fun run(part: DayPart = DayPart.BOTH) {
    when(part) {
      DayPart.PART1 -> println(part1FromResource())
      DayPart.PART2 -> println(part2FromResource())
      DayPart.BOTH -> {
        println(part1FromResource())
        println(part2FromResource())
      }
    }
  }
}

enum class DayPart {
  PART1, PART2, BOTH
}
