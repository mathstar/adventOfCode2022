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

class Day10Test : StandardTest(
  Day10(),
  """
    addx 15
    addx -11
    addx 6
    addx -3
    addx 5
    addx -1
    addx -8
    addx 13
    addx 4
    noop
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx -35
    addx 1
    addx 24
    addx -19
    addx 1
    addx 16
    addx -11
    noop
    noop
    addx 21
    addx -15
    noop
    noop
    addx -3
    addx 9
    addx 1
    addx -3
    addx 8
    addx 1
    addx 5
    noop
    noop
    noop
    noop
    noop
    addx -36
    noop
    addx 1
    addx 7
    noop
    noop
    noop
    addx 2
    addx 6
    noop
    noop
    noop
    noop
    noop
    addx 1
    noop
    noop
    addx 7
    addx 1
    noop
    addx -13
    addx 13
    addx 7
    noop
    addx 1
    addx -33
    noop
    noop
    noop
    addx 2
    noop
    noop
    noop
    addx 8
    noop
    addx -1
    addx 2
    addx 1
    noop
    addx 17
    addx -9
    addx 1
    addx 1
    addx -3
    addx 11
    noop
    noop
    addx 1
    noop
    addx 1
    noop
    noop
    addx -13
    addx -19
    addx 1
    addx 3
    addx 26
    addx -30
    addx 12
    addx -1
    addx 3
    addx 1
    noop
    noop
    noop
    addx -9
    addx 18
    addx 1
    addx 2
    noop
    noop
    addx 9
    noop
    noop
    noop
    addx -1
    addx 2
    addx -37
    addx 1
    addx 3
    noop
    addx 15
    addx -21
    addx 22
    addx -6
    addx 1
    noop
    addx 2
    addx 1
    noop
    addx -10
    noop
    noop
    addx 20
    addx 1
    addx 2
    addx 2
    addx -6
    addx -11
    noop
    noop
    noop
  """.trimIndent(),
  13140
) {
  @Test
  override fun part2Test() {
    assertEquals(
      ("""
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
      """.trimIndent() + "\n").lines(),
      (day.part2(input) as String).lines()
    )
  }
}

class Day11Test : StandardTest(
  Day11(),
  """
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
  """.trimIndent(),
  10605L,
  2713310158L
)

class Day12Test : StandardTest(
  Day12(),
  """
    Sabqponm
    abcryxxl
    accszExk
    acctuvwj
    abdefghi
  """.trimIndent(),
  31,
  29
)

class Day13Test : StandardTest(
  Day13(),
  """
    [1,1,3,1,1]
    [1,1,5,1,1]

    [[1],[2,3,4]]
    [[1],4]

    [9]
    [[8,7,6]]

    [[4,4],4,4]
    [[4,4],4,4,4]

    [7,7,7,7]
    [7,7,7]

    []
    [3]

    [[[]]]
    [[]]

    [1,[2,[3,[4,[5,6,7]]]],8,9]
    [1,[2,[3,[4,[5,6,0]]]],8,9]
  """.trimIndent(),
  13,
  140
)

class Day14Test : StandardTest(
  Day14(),
  """
    498,4 -> 498,6 -> 496,6
    503,4 -> 502,4 -> 502,9 -> 494,9
  """.trimIndent(),
  24,
  93
)

class Day15Test : StandardTest(
  Day15(10, 20),
  """
    Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    Sensor at x=9, y=16: closest beacon is at x=10, y=16
    Sensor at x=13, y=2: closest beacon is at x=15, y=3
    Sensor at x=12, y=14: closest beacon is at x=10, y=16
    Sensor at x=10, y=20: closest beacon is at x=10, y=16
    Sensor at x=14, y=17: closest beacon is at x=10, y=16
    Sensor at x=8, y=7: closest beacon is at x=2, y=10
    Sensor at x=2, y=0: closest beacon is at x=2, y=10
    Sensor at x=0, y=11: closest beacon is at x=2, y=10
    Sensor at x=20, y=14: closest beacon is at x=25, y=17
    Sensor at x=17, y=20: closest beacon is at x=21, y=22
    Sensor at x=16, y=7: closest beacon is at x=15, y=3
    Sensor at x=14, y=3: closest beacon is at x=15, y=3
    Sensor at x=20, y=1: closest beacon is at x=15, y=3
  """.trimIndent(),
  26,
  56000011L
)

class Day16Test : StandardTest(
  Day16(),
  """
    Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
    Valve BB has flow rate=13; tunnels lead to valves CC, AA
    Valve CC has flow rate=2; tunnels lead to valves DD, BB
    Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
    Valve EE has flow rate=3; tunnels lead to valves FF, DD
    Valve FF has flow rate=0; tunnels lead to valves EE, GG
    Valve GG has flow rate=0; tunnels lead to valves FF, HH
    Valve HH has flow rate=22; tunnel leads to valve GG
    Valve II has flow rate=0; tunnels lead to valves AA, JJ
    Valve JJ has flow rate=21; tunnel leads to valve II
  """.trimIndent(),
  1651,
  1707
)

class Day17Test : StandardTest(
  Day17(),
  ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>",
  3068L,
  1514285714288L
)

class Day18Test : StandardTest(
  Day18(),
  """
    2,2,2
    1,2,2
    3,2,2
    2,1,2
    2,3,2
    2,2,1
    2,2,3
    2,2,4
    2,2,6
    1,2,5
    3,2,5
    2,1,5
    2,3,5
  """.trimIndent(),
  64,
  58
)

class Day20Test : StandardTest(
  Day20(),
  """
    1
    2
    -3
    3
    -2
    0
    4
  """.trimIndent(),
  3,
  1623178306L
) {
  @Test
  fun additionalExample() {
    val l = "4, -2, 5, 6, 7, 8, 9".split(",").map { Day20.Datum(it.trim().toInt()) }
    val file = Day20.File(l)
    file.mix(listOf(l[1]))
    assertEquals(listOf(4,5,6,7,8,-2,9), file.l.map {it.value})
  }

  @Test
  fun duplicates() {
    val l = "1, 2, 1, 3, 4".split(",").map { Day20.Datum(it.trim().toInt()) }
    val file = Day20.File(l)
    file.mix(listOf(l[2]))
    assertEquals(listOf(1,2,3,1,4), file.l.map {it.value})
  }
}