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
    fun exec(initial: CpuState, listener: ((CpuState) -> Unit)? = null): CpuState {
      var state = initial
      for (i in 1 .. op.cycles) {
        state = state.incrementClock()
        listener?.invoke(state)
      }
      return when (op) {
        Op.NOOP -> state
        Op.ADDX -> state.addX(params[0].toInt())
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

  data class CpuState(val cycle: Int, val x: Int) {
    fun incrementClock(): CpuState = CpuState(cycle + 1, x)
    fun addX(param: Int): CpuState = CpuState(cycle, x + param)
  }

  private fun executeInstructions(instructions: List<Instruction>, listener: ((CpuState) -> Unit)? = null) {
    var state = CpuState(0, 1)
    instructions.forEach {
      state = it.exec(state, listener)
    }
  }

  override fun part1(input: String): Any {
    val instructions = input.lines().map { it.toInstruction() }

    val interestingCycles = arrayOf(20, 60, 100, 140, 180, 220)
    var total = 0
    executeInstructions(instructions) {state ->
      if (state.cycle in interestingCycles) total += state.cycle * state.x
    }
    return total
  }

  override fun part2(input: String): Any {
    val instructions = input.lines().map { it.toInstruction() }

    val screen = StringBuilder()
    var xPos = 0

    executeInstructions(instructions) {state ->
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
    return screen.toString()
  }
}