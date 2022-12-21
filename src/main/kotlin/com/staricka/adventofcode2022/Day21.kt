package com.staricka.adventofcode2022

import com.staricka.adventofcode2022.Day21.Monkey.Companion.parseMonkey
import java.util.Random

class Day21 : Day {
  override val id = 21

  interface Job {
    fun value(monkeys: Map<String, Monkey>): Long
    fun includesHuman(monkeys: Map<String, Monkey>): Boolean
  }

  class ConstantJob(private val value: Long) : Job {
    override fun value(monkeys: Map<String, Monkey>): Long = value
    override fun includesHuman(monkeys: Map<String, Monkey>): Boolean = false
  }

  enum class Op {
    PLUS, TIMES, MINUS, DIVIDE
  }

  // left and right fields for debugging
  @Suppress("unused")
  class DivisionException(val left: Long, val right: Long) : Exception()

  class OperationJob(val left: String, val right: String, val op: Op) : Job {
    override fun value(monkeys: Map<String, Monkey>): Long {
      val leftVal = monkeys[left]!!.job.value(monkeys)
      val rightVal = monkeys[right]!!.job.value(monkeys)
      return when(op) {
        Op.PLUS -> Math.addExact(leftVal, rightVal)
        Op.TIMES -> Math.multiplyExact(leftVal, rightVal)
        Op.MINUS -> Math.subtractExact(leftVal, rightVal)
        Op.DIVIDE -> {
          if (leftVal % rightVal != 0L) throw DivisionException(leftVal, rightVal)
          leftVal / rightVal
        }
      }
    }

    override fun includesHuman(monkeys: Map<String, Monkey>): Boolean =
      monkeys[left]!!.job.includesHuman(monkeys) ||
          monkeys[right]!!.job.includesHuman(monkeys)
  }

  class HumanJob(private val value: Long) : Job {
    override fun value(monkeys: Map<String, Monkey>): Long = value
    override fun includesHuman(monkeys: Map<String, Monkey>): Boolean = true
  }

  class Monkey(val id: String, val job: Job) {
    companion object {
      val opRegex = Regex("""([a-z]+): ([a-z]+) ([+-/*]) ([a-z]+)""")
      val constRegex = Regex("""([a-z]+): (-?[0-9]+)""")

      fun String.parseMonkey(): Monkey {
        var match = constRegex.matchEntire(this)
        if (match != null) {
          if (match.groups[1]!!.value == "humn") {
            return Monkey(match.groups[1]!!.value, HumanJob(match.groups[2]!!.value.toLong()))
          }
          return Monkey(
            match.groups[1]!!.value,
            ConstantJob(match.groups[2]!!.value.toLong())
          )
        }
        match = opRegex.matchEntire(this)!!
        return Monkey(
          match.groups[1]!!.value,
          OperationJob(
            match.groups[2]!!.value,
            match.groups[4]!!.value,
            when (match.groups[3]!!.value) {
              "+" -> Op.PLUS
              "*" -> Op.TIMES
              "-" -> Op.MINUS
              "/" -> Op.DIVIDE
              else -> throw Exception()
            }
          )
        )
      }
    }
  }

  override fun part1(input: String): Any {
    val monkeys = input.lines().map { it.parseMonkey() }.associateBy { it.id }

    return monkeys["root"]!!.job.value(monkeys)
  }

  override fun part2(input: String): Any {
    val monkeys = input.lines().map { it.parseMonkey() }.associateBy { it.id }.toMutableMap()

    val rootJob = monkeys["root"]!!.job as OperationJob

    val constant = if (!monkeys[rootJob.left]!!.job.includesHuman(monkeys))
      monkeys[rootJob.left]!!.job.value(monkeys)
    else
      monkeys[rootJob.right]!!.job.value(monkeys)

    val human = if (monkeys[rootJob.left]!!.job.includesHuman(monkeys))
      monkeys[rootJob.left]!!.job
    else
      monkeys[rootJob.right]!!.job

    var testVal = 4200L
    var a = -1L
    while (a < 0) {
      try {
        monkeys["humn"] = Monkey("humn", ConstantJob(testVal++))
        a = human.value(monkeys)
      } catch (_: Exception) {}
    }
    var b = -1L
    while (b < 0) {
      try {
        monkeys["humn"] = Monkey("humn", ConstantJob(testVal++))
        b = human.value(monkeys)
      } catch (_: Exception) {}
    }
    val increasing = a < b

    var lower = 0L
    var upper = Long.MAX_VALUE
    var pivot = upper / 2
    while (true) {
      monkeys["humn"] = Monkey("humn", ConstantJob(pivot))
      try {
        val result = human.value(monkeys)
        if (result == constant) return pivot
        if (result > constant) {
          if (increasing) {
            upper = pivot - 1L
          } else {
            lower = pivot + 1L
          }
        } else {
          if (increasing) {
            lower = pivot + 1L
          } else {
            upper = pivot - 1L
          }
        }

        pivot = upper - (upper - lower) / 2
      } catch (e: DivisionException) {
        pivot = Random().nextLong(lower, upper + 1)
      } catch (e: Exception) {
        // this feels like the wrong direction, but this is what works
        // TODO will revisit with not tired brain
        if (!increasing) {
          upper = pivot - 1L
        } else {
          lower = pivot + 1L
        }
        pivot = upper - (upper - lower) / 2
      }

    }
  }
}