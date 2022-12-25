package com.staricka.adventofcode2022

class Day25 : Day {
  override val id = 25

  private fun String.snafuToInt(): Long {
    var sum = 0L
    for ((i, c) in this.reversed().withIndex()) {
      var base = 1L
      for (j in 1..i) base *= 5L
      sum += base * when (c) {
        '2' -> 2L
        '1' -> 1L
        '0' -> 0L
        '-' -> -1L
        '=' -> -2L
        else -> throw Exception()
      }
    }
    return sum
  }

  private fun Long.toSnafu(): String {
    val base5 = this.toString(5)
    val sb = StringBuilder()
    var carry = 0
    for (c in base5.reversed()) {
      var digit = c.digitToInt() + carry
      carry = 0
      while (digit > 2) {
        carry++
        digit -= 5
      }
      sb.append(when (digit) {
        2 -> '2'
        1 -> '1'
        0 -> '0'
        -1 -> '-'
        -2 -> '='
        else -> throw Exception()
      })
    }
    if (carry > 0) sb.append(carry.digitToChar())
    return sb.reverse().toString()
  }

  override fun part1(input: String) = input.lines().sumOf { it.snafuToInt() }.toSnafu()

  override fun part2(input: String) = ""
}