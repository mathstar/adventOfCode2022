package com.staricka.adventofcode2022

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

abstract class StandardTest(
  val day: Day,
  val input: String,
  private val part1Output: Any? = null,
  private val part2Output: Any? = null
) {
  @Test
  fun part1Test() {
    assertEquals(part1Output, day.part1(input))
  }

  @Test
  open fun part2Test() {
    assertEquals(part2Output, day.part2(input))
  }
}