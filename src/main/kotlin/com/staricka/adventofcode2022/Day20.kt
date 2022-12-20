package com.staricka.adventofcode2022

import com.staricka.adventofcode2022.Day20.File.Companion.parseFile

class Day20 : Day {
  override val id = 20

  data class Datum(val value: Int, val id: Int = nextId++) {
    val keyedValue = value.toLong() * KEY

    companion object {
      const val KEY = 811589153L
      var nextId = 0
    }
  }

  class File(var l: List<Datum>) {
    fun mix(order: List<Datum> = l) {
      val lp = ArrayList<Datum>(l)
      for (n in order) {
        var index = lp.indexOf(n)

        index += n.value

        lp.remove(n)
        index %= lp.size
        if (index < 0) index += lp.size
        lp.add(index, n)
      }
      l = lp
    }

    fun mixWithKey(order: List<Datum> = l) {
      val lp = ArrayList<Datum>(l)
      for (n in order) {
        var index = lp.indexOf(n).toLong()

        index += n.keyedValue

        lp.remove(n)
        index %= lp.size
        if (index < 0) index += lp.size
        lp.add(index.toInt(), n)
      }
      l = lp
    }

    fun coordSum(): Int {
      val base = l.withIndex().first { it.value.value == 0 }.index
      return l[(base + 1000) % l.size].value + l[(base + 2000) % l.size].value + l[(base + 3000) % l.size].value
    }

    fun coordSumWithKey(): Long {
      val base = l.withIndex().first { it.value.value == 0 }.index
      return l[(base + 1000) % l.size].keyedValue + l[(base + 2000) % l.size].keyedValue + l[(base + 3000) % l.size].keyedValue
    }

    companion object {
      fun String.parseFile() = File(
        this.lines().map { Datum(it.toInt()) }
      )
    }
  }

  override fun part1(input: String): Any {
    val file = input.parseFile()
    file.mix()
    return file.coordSum()
  }

  override fun part2(input: String): Any {
    val file = input.parseFile()
    val mixOrder = file.l
    for (i in 1..10) file.mixWithKey(mixOrder)
    return file.coordSumWithKey()
  }
}