package com.staricka.adventofcode2022

import com.staricka.adventofcode2022.Day5.Stacks.Companion.parseToStacks

class Day5 : Day {
  override val id = 5

  class Stacks(n: Int) {
    val stacks = ArrayList<ArrayDeque<Char>>(n)
    init {
      for (x in 1..n) {
        stacks.add(ArrayDeque())
      }
    }

    fun apply(move: Move) {
      for (i in 1..move.count) {
        stacks[move.dest - 1].addLast(stacks[move.source - 1].removeLast())
      }
    }

    fun applyPart2(move: Move) {
      val moving = ArrayList<Char>()
      for (i in 1..move.count) {
        moving.add(stacks[move.source - 1].removeLast())
      }
      moving.reversed().forEach{stacks[move.dest - 1].addLast(it)}
    }

    companion object {
      fun String.parseToStacks(): Stacks {
        val count = this.lines().first().length / 4 + 1
        val stacks = Stacks(count)
        for (line in this.lines()) {
          if (!line.trim().startsWith('[')) continue
          for (i in 0 until count) {
            val crate = line[1 + 4 * i]
            if (!crate.isWhitespace()) {
              stacks.stacks[i].addFirst(crate)
            }
          }
        }
        return stacks
      }
    }
  }

  data class Move(val count: Int, val source: Int, val dest: Int) {
    companion object {
      val MOVE_REGEX = Regex("move ([0-9]+) from ([0-9]+) to ([0-9]+)")
    }
  }

  private fun parseInput(input: String): Pair<Stacks, List<Move>> {
    var stacksInput = ""
    val moves = ArrayList<Move>()

    var inStacks = true
    for (line in input.lines()) {
      if (inStacks) {
        if (line.isBlank()) {
          inStacks = false
          continue
        }
        stacksInput += line + "\n"
      } else {
        val match = Move.MOVE_REGEX.matchEntire(line)
        moves.add(Move(match!!.groups[1]!!.value.toInt(), match.groups[2]!!.value.toInt(), match.groups[3]!!.value.toInt()))
      }
    }
    return Pair(stacksInput.parseToStacks(), moves)
  }

  override fun part1(input: String): Any {
    val (stacks, moves) = parseInput(input)
    moves.forEach {stacks.apply(it)}

    return stacks.stacks.map {it.last()}.joinToString(separator = "")
  }

  override fun part2(input: String): Any {
    val (stacks, moves) = parseInput(input)
    moves.forEach {stacks.applyPart2(it)}

    return stacks.stacks.map {it.last()}.joinToString(separator = "")
  }
}