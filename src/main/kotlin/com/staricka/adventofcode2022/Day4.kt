package com.staricka.adventofcode2022

class Day4 : Day {
  override val id = 4

  override fun part1(input: String): Any =
    input.lines().count {
      val first = it.split(",")[0].split("-").map(String::toInt)
      val second = it.split(",")[1].split("-").map(String::toInt)

      (first[0] <= second[0] && first[1] >= second[1]) || (second[0] <= first[0] && second[1] >= first[1])
    }

  override fun part2(input: String): Any =
    input.lines().count {
      val first = it.split(",")[0].split("-").map(String::toInt)
      val second = it.split(",")[1].split("-").map(String::toInt)

      (first[0] <= second[0] && first[1] >= second[0]) || (first[0] <= second[1] && first[1] >= second[1])
          || (second[0] <= first[0] && second[1] >= first[0]) || (second[0] <= first[1] && second[1] >= first[1])
    }
}
