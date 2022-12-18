package com.staricka.adventofcode2022

import kotlin.test.assertEquals

class Day1RegressionTest : RegressionTest(
  Day1(),
  66186,
  196804
)

class Day2RegressionTest : RegressionTest(
  Day2(),
  14375,
  10274
)

class Day3RegressionTest : RegressionTest(
  Day3(),
  7980,
  2881
)

class Day4RegressionTest : RegressionTest(
  Day4(),
  477,
  830
)

class Day5RegressionTest : RegressionTest(
  Day5(),
  "WSFTMRHPP",
  "GSLCMFBRP"
)

class Day6RegressionTest : RegressionTest(
  Day6(),
  1480,
  2746
)

class Day7RegressionTest : RegressionTest(
  Day7(),
  1989474,
  1111607
)

class Day8RegressionTest : RegressionTest(
  Day8(),
  1845,
  230112
)

class Day9RegressionTest : RegressionTest(
  Day9(),
  6067,
  2471
)

class Day10RegressionTest : RegressionTest(
  Day10(),
  11780,
  """
    ###..####.#..#.#....###...##..#..#..##..
    #..#....#.#..#.#....#..#.#..#.#..#.#..#.
    #..#...#..#..#.#....###..#..#.#..#.#..#.
    ###...#...#..#.#....#..#.####.#..#.####.
    #....#....#..#.#....#..#.#..#.#..#.#..#.
    #....####..##..####.###..#..#..##..#..#.
  """.trimIndent() + "\n"
) {
  override fun part2RegressionTest() {
    assertEquals((part2Output as String).lines(), day.part2FromResource())
  }
}

class Day11RegressionTest : RegressionTest(
  Day11(),
  95472L,
  17926061332L
)

class Day12RegressionTest : RegressionTest(
  Day12(),
  447,
  446
)

class Day13RegressionTest : RegressionTest(
  Day13(),
  5588,
  23958
)

class Day14RegressionTest : RegressionTest(
  Day14(),
  728,
  27623
)

class Day15RegressionTest : RegressionTest(
  Day15(),
  5166077,
  13071206703981L
)

class Day16RegressionTest : RegressionTest(
  Day16(),
  1947,
  2556
)

class Day17RegressionTest : RegressionTest(
  Day17(),
  3235L,
  1591860465110L
)

class Day18RegressionTest : RegressionTest(
  Day18(),
  3498,
  2008
)