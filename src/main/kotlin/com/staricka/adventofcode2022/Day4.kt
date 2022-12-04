package com.staricka.adventofcode2022

class Day4 : Day {
  override val id = 4

  override fun part1(input: String): Any? {
    return input.lines().filter {
      val first = it.split(",")[0].split("-").map{ it.toInt() }
      val second = it.split(",")[1].split("-").map{ it.toInt() }

      (first[0] <= second[0] && first[1] >= second[1]) || (second[0] <= first[0] && second[1] >= first[1])
    }.count()
  }

  override fun part2(input: String): Any? {
    return input.lines().filter {
      val first = it.split(",")[0].split("-").map{ it.toInt() }
      val second = it.split(",")[1].split("-").map{ it.toInt() }

      (first[0] <= second[0] && first[1] >= second[0]) || (first[0] <= second[1] && first[1] >= second[1])
          || (second[0] <= first[0] && second[1] >= first[0]) || (second[0] <= first[1] && second[1] >= first[1])
    }.count()
  }
}

fun main() {
  println(Day4().part1("""
    2-4,6-8
    2-3,4-5
    5-7,7-9
    2-8,3-7
    6-6,4-6
    2-6,4-8
  """.trimIndent()))
}