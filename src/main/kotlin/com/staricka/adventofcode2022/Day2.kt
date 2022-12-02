package com.staricka.adventofcode2022

import java.lang.Exception

class Day2 : Day {
  override val id = 2

  enum class Choice(val score: Int) {
    ROCK(1), PAPER(2), SCISSORS(3);

    fun outcome(vs: Choice) : Int{
      if (this == vs) return 3
      if (this == ROCK && vs == SCISSORS) return 6
      if (this == PAPER && vs == ROCK) return 6
      if (this == SCISSORS && vs == PAPER) return 6
      return 0
    }

    fun makeChoice(desiredResult: Char): Choice =
      when(desiredResult) {
        'X' -> when(this) {
          ROCK -> SCISSORS
          PAPER -> ROCK
          SCISSORS -> PAPER
        }
        'Y' -> this
        'Z' -> when(this) {
          ROCK -> PAPER
          PAPER -> SCISSORS
          SCISSORS -> ROCK
        }
        else -> throw Exception()
      }

    companion object {
      fun fromChar(c: Char) : Choice =
        when (c) {
          'A' -> ROCK
          'B' -> PAPER
          'C' -> SCISSORS
          'X' -> ROCK
          'Y' -> PAPER
          'Z' -> SCISSORS
          else -> throw Exception()
        }
    }
  }

  override fun part1(input: String): Any {
    return input.lines().filter { it.isNotBlank() }.map {
      val opponent = Choice.fromChar(it.split(" ")[0][0])
      val mine = Choice.fromChar(it.split(" ")[1][0])

      mine.score + mine.outcome(opponent)
    }.sum()
  }

  override fun part2(input: String): Any =
    input.lines().filter { it.isNotBlank() }.map {
      val opponent = Choice.fromChar(it.split(" ")[0][0])
      val mine = opponent.makeChoice(it.split(" ")[1][0])

      mine.score + mine.outcome(opponent)
    }.sum()
}