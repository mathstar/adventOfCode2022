package com.staricka.adventofcode2022

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