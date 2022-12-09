package com.staricka.adventofcode2022

import com.staricka.adventofcode2022.Day9.Move.Companion.toMove
import kotlin.math.abs

class Day9 : Day {
  override val id = 9

  private fun computeTailMovement(head: Pair<Int, Int>, tail: Pair<Int, Int>): Pair<Int, Int> {
    val (hx, hy) = head
    val (tx, ty) = tail
    return if (abs(hx - tx) <= 1 && abs(hy - ty) <= 1) {
      tail
    } else if (hx == tx) {
      val tyNew = ty + (if (hy - ty > 0) 1 else -1)
      Pair(tx, tyNew)
    } else if (hy == ty) {
      val txNew = tx + (if (hx - tx > 0) 1 else -1)
      Pair(txNew, ty)
    } else {
      val txNew = tx + (if (hx - tx > 0) 1 else -1)
      val tyNew = ty + (if (hy - ty > 0) 1 else -1)
      Pair(txNew, tyNew)
    }
  }

  enum class Direction(val xStep: Int, val yStep: Int) {
    L(-1, 0), R(1, 0), U(0, 1), D(0, -1)
  }

  data class Move(val direction: Direction, var distance: Int) {
    fun step(initial: Pair<Int, Int>): Pair<Int, Int> {
      if (distance <= 0) return initial
      val result = Pair(initial.first + direction.xStep, initial.second + direction.yStep)
      distance--
      return result
    }

    companion object {
      fun String.toMove() : Move {
        val split = this.split(" ")
        return Move(Direction.valueOf(split[0]), split[1].toInt())
      }
    }
  }

  override fun part1(input: String): Any {
    var head = Pair(0,0)
    var tail = Pair(0,0)
    val tailPositions = HashSet<Pair<Int, Int>>()
    tailPositions.add(tail)

    input.lines().map { it.toMove() }.forEach { move ->
      while (move.distance > 0) {
        head = move.step(head)
        tail = computeTailMovement(head, tail)
        tailPositions.add(tail)
      }
    }

    return tailPositions.size
  }

  override fun part2(input: String): Any {
    val positions = Array(10) { Pair(0,0) }
    val tailPositions = HashSet<Pair<Int, Int>>()
    tailPositions.add(positions[9])

    input.lines().map { it.toMove() }.forEach { move ->
      while (move.distance > 0) {
        positions[0] = move.step(positions[0])
        for (i in 1..9) {
          positions[i] = computeTailMovement(positions[i - 1], positions[i])
        }
        tailPositions.add(positions[9])
      }
    }
    return tailPositions.size
  }
}