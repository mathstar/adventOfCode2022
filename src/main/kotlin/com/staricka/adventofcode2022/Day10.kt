package com.staricka.adventofcode2022

import com.staricka.adventofcode2022.Day10.Instruction.Companion.toInstruction
import kotlin.math.abs

class Day10 : Day {
  override val id = 10

  enum class Op(val cycles: Int) {
    NOOP(1), ADDX(2)
  }

  data class Instruction(
    val op: Op,
    val params: List<String>
  ) {
    fun exec(initial: CpuState): CpuState {
      when (op) {
        Op.NOOP -> return initial
        Op.ADDX -> return CpuState(initial.x + params[0].toInt())
      }
    }

    companion object {
      fun String.toInstruction(): Instruction {
        val split = this.split(" ")
        return Instruction(
          Op.valueOf(split[0].uppercase()),
          if (split.size > 1) split.subList(1, split.size) else emptyList()
        )
      }
    }
  }

  data class CpuState(val x: Int)

  override fun part1(input: String): Any {
    val interestingCycles = arrayOf(20, 60, 100, 140, 180, 220)

    val instructions = input.lines().map { it.toInstruction() }.iterator()
    var state = CpuState(1)
    var cycle = 0

    var total = 0
    while (instructions.hasNext()) {
      val instruction = instructions.next()
      for (i in 1 .. instruction.op.cycles) {
        cycle++
        if (cycle in interestingCycles) total += cycle * state.x
      }
      state = instruction.exec(state)
    }
    return total
  }

  override fun part2(input: String): Any {
    val instructions = input.lines().map { it.toInstruction() }.iterator()
    var state = CpuState(1)
    var cycle = 0

    val screen = StringBuilder()
    var xPos = 0

    while (instructions.hasNext()) {
      val instruction = instructions.next()
      for (i in 1 .. instruction.op.cycles) {
        cycle++
        if (abs(state.x - xPos) <= 1) {
          screen.append('#')
        } else {
          screen.append('.')
        }
        xPos++
        if (xPos >= 40) {
          xPos = 0
          screen.append(System.lineSeparator())
        }
      }
      state = instruction.exec(state)
    }
    return screen.toString()
  }
}