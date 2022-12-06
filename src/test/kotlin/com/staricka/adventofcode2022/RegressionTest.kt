package com.staricka.adventofcode2022

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

abstract class RegressionTest(
  private val day: Day,
  private val part1Output: Any?,
  private val part2Output: Any?
) {
  @Test
  fun part1RegressionTest() {
    assertEquals(part1Output, day.part1FromResource())
  }

  @Test
  fun part2RegressionTest() {
    assertEquals(part2Output, day.part2FromResource())
  }
}