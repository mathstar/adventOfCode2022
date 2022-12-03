package com.staricka.adventofcode2022

@Suppress("unused")
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
      val chars = it.toCharArray()
      val left = HashSet<Char>()
      val right = HashSet<Char>()
      for (i in 0 until chars.size / 2) {
        left.add(chars[i])
        right.add(chars[chars.size / 2 + i])
      }

      left.intersect(right).first().itemToPriority()
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

fun main() {
  println(Day3().part1(
    """
      vJrwpWtwJgWrhcsFMMfFFhFp
      jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
      PmmdzqPrVvPwwTWBwg
      wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
      ttgJtRGJQctTZtZT
      CrZsJsPPZsGzwwsLwLmpwMDw
    """.trimIndent()
  ))
}