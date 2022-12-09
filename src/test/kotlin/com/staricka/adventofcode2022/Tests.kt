package com.staricka.adventofcode2022

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day1Test : StandardTest(
  Day1(),
  """
    1000
    2000
    3000

    4000

    5000
    6000

    7000
    8000
    9000

    10000
  """.trimIndent(),
  24000,
  45000
)

class Day2Test : StandardTest(
  Day2(),
  """
    A Y
    B X
    C Z
  """.trimIndent(),
  15,
  12
)

class Day3Test : StandardTest(
  Day3(),
  """
    vJrwpWtwJgWrhcsFMMfFFhFp
    jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
    PmmdzqPrVvPwwTWBwg
    wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
    ttgJtRGJQctTZtZT
    CrZsJsPPZsGzwwsLwLmpwMDw
  """.trimIndent(),
  157,
  70
)

class Day4Test : StandardTest(
  Day4(),
  """
    2-4,6-8
    2-3,4-5
    5-7,7-9
    2-8,3-7
    6-6,4-6
    2-6,4-8
  """.trimIndent(),
  2,
  4
)

class Day5Test : StandardTest(
  Day5(),
  """
        [D]    
    [N] [C]    
    [Z] [M] [P]
     1   2   3 
    
    move 1 from 2 to 1
    move 3 from 1 to 3
    move 2 from 2 to 1
    move 1 from 1 to 2
  """.trimIndent(),
  "CMZ",
  "MCD"
)

class Day6Test : StandardTest(
  Day6(),
  "mjqjpqmgbljsphdztnvjfqwrcgsmlb",
  7,
  19
)

class Day7Test : StandardTest(
  Day7(),
  """
    ${'$'} cd /
    ${'$'} ls
    dir a
    14848514 b.txt
    8504156 c.dat
    dir d
    ${'$'} cd a
    ${'$'} ls
    dir e
    29116 f
    2557 g
    62596 h.lst
    ${'$'} cd e
    ${'$'} ls
    584 i
    ${'$'} cd ..
    ${'$'} cd ..
    ${'$'} cd d
    ${'$'} ls
    4060174 j
    8033020 d.log
    5626152 d.ext
    7214296 k
  """.trimIndent(),
  95437,
  24933642
)

class Day8Test : StandardTest(
  Day8(),
  """
    30373
    25512
    65332
    33549
    35390
  """.trimIndent(),
  21,
  8
)

class Day9Test : StandardTest(
  Day9(),
  """
    R 4
    U 4
    L 3
    D 1
    R 4
    D 1
    L 5
    R 2
  """.trimIndent(),
  13,
  1
) {
  @Test
  fun part2ExtendedTest() {
    assertEquals(36, day.part2("""
      R 5
      U 8
      L 8
      D 3
      R 17
      D 10
      L 25
      U 20
    """.trimIndent()))
  }
}