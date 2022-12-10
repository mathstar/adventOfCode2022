package com.staricka.adventofcode2022

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

abstract class RegressionTest(
  val day: Day,
  val part1Output: Any?,
  val part2Output: Any?
) {
  @Test
  open fun part1RegressionTest() {
    assertEquals(part1Output, day.part1FromResource())
  }

  @Test
  open fun part2RegressionTest() {
    assertEquals(part2Output, day.part2FromResource())
  }
}