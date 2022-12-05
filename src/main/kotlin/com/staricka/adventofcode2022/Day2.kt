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
  }

  private fun String.toChoice() : Choice =
    when (this[0]) {
      'A' -> Choice.ROCK
      'B' -> Choice.PAPER
      'C' -> Choice.SCISSORS
      'X' -> Choice.ROCK
      'Y' -> Choice.PAPER
      'Z' -> Choice.SCISSORS
      else -> throw Exception()
    }

  private fun String.lineToPart1Choices() : Pair<Choice, Choice> {
    val split = this.split(" ")
    return Pair(split[0].toChoice(), split[1].toChoice())
  }

  private fun String.lineToPart2Choices() : Pair<Choice, Choice> {
    val split = this.split(" ")
    val opponent = split[0].toChoice()
    return Pair(opponent, opponent.makeChoice(split[1][0]))
  }

  override fun part1(input: String): Any {
    return input.lines().filter { it.isNotBlank() }.map {
      val (opponent, mine) = it.lineToPart1Choices()
      mine.score + mine.outcome(opponent)
    }.sum()
  }

  override fun part2(input: String): Any =
    input.lines().filter { it.isNotBlank() }.map {
      val (opponent, mine) = it.lineToPart2Choices()
      mine.score + mine.outcome(opponent)
    }.sum()
}