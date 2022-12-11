package com.staricka.adventofcode2022

import java.lang.Exception

class Day11 : Day {
  override val id = 11

  data class Item(val worryLevel: Long)

  interface Operand {
    fun value(old: Long): Long

    companion object {
      fun fromString(input: String) : Operand =
        when(input) {
          "old" -> OldOperand()
          else -> NumberOperand(input.toLong())
        }
    }
  }

  class OldOperand : Operand {
    override fun value(old: Long): Long = old
  }
  class NumberOperand(private val value: Long) : Operand {
    override fun value(old: Long): Long = value
  }

  enum class Op {
    PLUS, TIMES;

    fun apply(left: Long, right: Long): Long =
      when(this) {
        PLUS -> Math.addExact(left, right)
        TIMES -> Math.multiplyExact(left, right)
      }

    companion object {
      fun fromString(input: String) =
        when(input) {
          "+" -> PLUS
          "*" -> TIMES
          else -> throw Exception()
        }
    }
  }

  data class WorryOperation(
    val left: Operand,
    val op: Op,
    val right: Operand
  ) {
    fun perform(old: Long): Long = op.apply(left.value(old), right.value(old))
  }

  data class WorryTest (
    val divisor: Long,
    val trueDestination: Int,
    val falseDestination: Int
  ) {
    fun perform(worryLevel: Long): Int = if (worryLevel % divisor == 0L) trueDestination else falseDestination
  }

  class Monkey(
    val items: ArrayDeque<Item>,
    val operation: WorryOperation,
    val test: WorryTest
  ) {
    var inspectedCount = 0L

    fun executeTurn(monkeys: Map<Int, Monkey>, worryReductionFunction: (Long) -> Long = {it / 3}) {
      while (items.isNotEmpty()) {
        val item = items.removeFirst()
        inspectedCount++
        var worry = item.worryLevel
        worry = operation.perform(worry)
        worry = worryReductionFunction.invoke(worry)
        val destination = test.perform(worry)
        monkeys[destination]!!.items.addLast(Item(worry))
      }
    }
  }

  private val monkeyRegex = Regex("""Monkey ([0-9]+):""")
  private val startingItemsRegex = Regex(""" *Starting items: ([0-9]+(, [0-9]+)*)""")
  private val operationRegex = Regex(""" *Operation: new = ([a-z0-9]+) ([*+]) ([a-z0-9]+)""")
  private val testRegex = Regex(""" *Test: divisible by ([0-9]+)""")
  private val testTrueRegex = Regex(""" *If true: throw to monkey ([0-9]+)""")
  private val testFalseRegex = Regex(""" *If false: throw to monkey ([0-9]+)""")
  private fun parseInput(input: String): Map<Int, Monkey> {
    val monkeys = HashMap<Int, Monkey>()
    val iter = input.lines().iterator()
    while (iter.hasNext()) {
      var line = iter.next()
      while (line.isBlank()) {
        if (!iter.hasNext()) break
        line = iter.next()
      }

      val monkeyMatch = monkeyRegex.matchEntire(line)!!
      line = iter.next()
      val startingItemMatch = startingItemsRegex.matchEntire(line)!!
      line = iter.next()
      val operationMatch = operationRegex.matchEntire(line)!!
      line = iter.next()
      val testMatch = testRegex.matchEntire(line)!!
      line = iter.next()
      val trueMatch = testTrueRegex.matchEntire(line)!!
      line = iter.next()
      val falseMatch = testFalseRegex.matchEntire(line)!!

      monkeys[monkeyMatch.groups[1]!!.value.toInt()] = Monkey (
        startingItemMatch.groups[1]!!.value.split(", ").map { Item(it.toLong()) }.toCollection(ArrayDeque()),
        WorryOperation(
          Operand.fromString(operationMatch.groups[1]!!.value),
          Op.fromString(operationMatch.groups[2]!!.value),
          Operand.fromString(operationMatch.groups[3]!!.value)
        ),
        WorryTest(
          testMatch.groups[1]!!.value.toLong(),
          trueMatch.groups[1]!!.value.toInt(),
          falseMatch.groups[1]!!.value.toInt()
        )
      )
    }

    return monkeys
  }

  private val product = { acc: Long, a: Long -> acc * a }

  override fun part1(input: String): Any {
    val monkeys = parseInput(input)
    for (i in 1..20) {
      for (m in 0 until monkeys.size) {
        monkeys[m]!!.executeTurn(monkeys)
      }
    }

    return monkeys.values.map { it.inspectedCount }.sortedDescending().take(2).reduce(product)
  }

  override fun part2(input: String): Any {
    val monkeys = parseInput(input)
    val modulus = monkeys.values.map { it.test.divisor }.reduce(product)

    for (i in 1..10000) {
      for (m in 0 until monkeys.size) {
        monkeys[m]!!.executeTurn(monkeys) {it % modulus}
      }
    }

    return monkeys.values.map { it.inspectedCount }.sortedDescending().take(2).reduce(product)
  }
}