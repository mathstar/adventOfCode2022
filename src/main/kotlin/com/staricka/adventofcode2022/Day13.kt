package com.staricka.adventofcode2022

class Day13 : Day {
  override val id = 13

  interface Packet {
    fun compareTo(other: Packet): Int {
      if (this is IntPacket && other is IntPacket) {
        return this.value - other.value
      }

      val thisList = if (this is ListPacket) this else ListPacket(mutableListOf(this))
      val otherList = if (other is ListPacket) other else ListPacket(mutableListOf(other))

      for (i in thisList.value.indices) {
        if (i >= otherList.value.size) {
          return 1
        }
        val compare = thisList.value[i].compareTo(otherList.value[i])
        if (compare != 0) return compare
      }
      return if (thisList.value.size < otherList.value.size) -1 else 0
    }
  }

  class IntPacket(val value: Int) : Packet

  class ListPacket(val value: MutableList<Packet> = ArrayList()) : Packet

  private fun parsePacket(input: String) : Packet {
    if (!input.startsWith('[')) return IntPacket(input.toInt())

    val stack = ArrayDeque<ListPacket>()
    val head = ListPacket()
    stack.add(head)
    val acc = StringBuilder()
    for (c in input) {
      if (c.isDigit()) {
        acc.append(c)
      } else {
        if (acc.isNotEmpty()) {
          stack.last().value.add(IntPacket(acc.toString().toInt()))
          acc.clear()
        }
      }
      when (c) {
        '[' -> {
          val p = ListPacket()
          stack.last().value.add(p)
          stack.add(p)
        }
        ']' -> {
          stack.removeLast()
        }
      }
    }
    return head.value.first()
  }

  override fun part1(input: String): Any {
    val lines = input.lines().toMutableList()

    var index = 0
    var sum = 0
    while (lines.isNotEmpty()) {
      while (lines.first().isBlank()) lines.removeFirst()

      index++
      val packet1 = parsePacket(lines.removeFirst())
      val packet2 = parsePacket(lines.removeFirst())
      if (packet1.compareTo(packet2) < 0) sum += index
    }

    return sum
  }

  override fun part2(input: String): Any {
    val divider1 = parsePacket("[[2]]")
    val divider2 = parsePacket("[[6]]")
    val lines = input.lines()
      .filter { it.isNotBlank() }
      .map { parsePacket(it) }
      .toMutableList()
      .also {
      it.add(divider1)
      it.add(divider2)
      }.sortedWith(Packet::compareTo)

    return (lines.indexOf(divider1) + 1) * (lines.indexOf(divider2) + 1)
  }
}