package com.staricka.adventofcode2022

import kotlin.math.max
import kotlin.math.min

class Day22(val faceSize: Int = 50) : Day {
  override val id = 22

  enum class Turn {
    L, R
  }

  enum class Heading {
    RIGHT, DOWN, LEFT, UP;

    fun turn(t: Turn): Heading {
      return when(t) {
        Turn.L -> values()[(this.ordinal - 1 + values().size) % values().size]
        Turn.R -> values()[(this.ordinal + 1) % values().size]
      }
    }

    fun step(coord: Pair<Int, Int>): Pair<Int, Int> =
      when(this) {
        RIGHT -> Pair(coord.first, coord.second + 1)
        DOWN -> Pair(coord.first + 1, coord.second)
        LEFT -> Pair(coord.first, coord.second - 1)
        UP -> Pair(coord.first - 1, coord.second)
      }
  }

  interface Instruction

  data class TurnInstruction(val turn: Turn) : Instruction
  data class StepInstruction(val steps: Int) : Instruction

  enum class Tile {
    BLANK, AIR, WALL
  }

  data class Face(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int) {
    val id = nextId++
    companion object {
      var nextId = 0
    }
  }
  data class Adjacency(
    val face: Face,
    val newHeading: Heading,
    val translation: (Pair<Int, Int>) -> Pair<Int, Int>
  )

  data class AdjacentFaces(val right: Adjacency, val down: Adjacency, val left: Adjacency, val up: Adjacency)

  class Grid(faceSize: Int) {
    val tiles = HashMap<Int, HashMap<Int, Tile>>()
    var minRow0 = Int.MAX_VALUE
    var maxX = 0
    var maxY = 0

    fun get(x: Int, y: Int) = tiles[x]?.get(y) ?: Tile.BLANK

    fun put(x: Int, y: Int, t: Tile) {
      tiles.computeIfAbsent(x) {HashMap()}[y] = t
      if (x == 0 && t != Tile.BLANK) minRow0 = min(minRow0, y)
      maxX = max(maxX, x)
      maxY = max(maxY, y)
    }

    val coordFaceMap = HashMap<Int, HashMap<Int, Face>>()
    val faceAdjacencyMap = HashMap<Face, AdjacentFaces>()

    // represents a coordinate's behavior crossing a seam
    enum class CoordTranslation {
      PLUS_X, // coordinate is distance from origin of source's x
      MINUS_X, // coordinate is distance from far side of source's x
      PLUS_Y, // ditto using source's y
      MINUS_Y, // ditto using source's y
      MIN, // use the destination's min value for this coordinate
      MAX // ditto using max
    }

    // builds a translation function to step across a folded boundary
    fun tranlation(
      xTranslation: CoordTranslation,
      yTranslation: CoordTranslation,
      source: Face,
      dest: Face
    ): (Pair<Int, Int>) -> Pair<Int, Int> {
      return { (x, y) ->
        val xDiff = x - source.xMin
        val yDiff = y - source.yMin

        Pair(
          when (xTranslation) {
            CoordTranslation.PLUS_X -> xDiff + dest.xMin
            CoordTranslation.MINUS_X -> dest.xMax - xDiff
            CoordTranslation.PLUS_Y -> yDiff + dest.xMin
            CoordTranslation.MINUS_Y -> dest.xMax - yDiff
            CoordTranslation.MIN -> dest.xMin
            CoordTranslation.MAX -> dest.xMax
          },
          when (yTranslation) {
            CoordTranslation.PLUS_X -> xDiff + dest.yMin
            CoordTranslation.MINUS_X -> dest.yMax - xDiff
            CoordTranslation.PLUS_Y -> yDiff + dest.yMin
            CoordTranslation.MINUS_Y -> dest.yMax - yDiff
            CoordTranslation.MIN -> dest.yMin
            CoordTranslation.MAX -> dest.yMax
          }
        )
      }
    }

    // represents a translation across faces adjacent on the flat surface
    // TODO probably not needed
    fun flatTranslation(heading: Heading): (Pair<Int, Int>) -> Pair<Int, Int> {
      return { coord -> heading.step(coord) }
    }

    // TODO figure out face boundaries programmatically
    fun identifyFaces() {
      // HARDCODED for example
      val faces = listOf(
        Face(0, 3, 8, 11),
        Face(4, 7, 0, 3),
        Face(4, 7, 4, 7),
        Face(4, 7, 8, 11),
        Face(8, 11, 8, 11),
        Face(8, 11, 12, 15)
      )

      for (face in faces) {
        for (x in face.xMin..face.xMax) {
          for (y in face.yMin..face.yMax) {
            coordFaceMap.computeIfAbsent(x) {HashMap()}[y] = face
          }
        }
      }

      faceAdjacencyMap[faces[0]] = AdjacentFaces(
        Adjacency(faces[5], Heading.LEFT, tranlation(CoordTranslation.MINUS_X, CoordTranslation.MAX, faces[0], faces[5])),
        Adjacency(faces[3], Heading.DOWN, tranlation(CoordTranslation.MIN, CoordTranslation.PLUS_Y, faces[0], faces[3])),
        Adjacency(faces[2], Heading.DOWN, tranlation(CoordTranslation.MIN, CoordTranslation.PLUS_X, faces[0], faces[2])),
        Adjacency(faces[1], Heading.DOWN, tranlation(CoordTranslation.MIN, CoordTranslation.MINUS_Y, faces[0], faces[1]))
      )
      faceAdjacencyMap[faces[1]] = AdjacentFaces(
        Adjacency(faces[2], Heading.RIGHT, flatTranslation(Heading.RIGHT)),
        Adjacency(faces[4], Heading.UP, tranlation(CoordTranslation.MAX, CoordTranslation.MINUS_Y, faces[1], faces[4])),
        Adjacency(faces[5], Heading.UP, tranlation(CoordTranslation.MAX, CoordTranslation.MINUS_X, faces[1], faces[5])),
        Adjacency(faces[0], Heading.DOWN, tranlation(CoordTranslation.MIN, CoordTranslation.MINUS_Y, faces[0], faces[1]))
      )
      faceAdjacencyMap[faces[2]] = AdjacentFaces(
        Adjacency(faces[3], Heading.RIGHT, flatTranslation(Heading.RIGHT)),
        Adjacency(faces[4], Heading.RIGHT, tranlation(CoordTranslation.MINUS_Y, CoordTranslation.MIN, faces[2], faces[4])),
        Adjacency(faces[1], Heading.LEFT, flatTranslation(Heading.LEFT)),
        Adjacency(faces[0], Heading.LEFT, tranlation(CoordTranslation.PLUS_Y, CoordTranslation.MIN, faces[2], faces[0]))
      )
      faceAdjacencyMap[faces[3]] = AdjacentFaces(
        Adjacency(faces[5], Heading.DOWN, tranlation(CoordTranslation.MIN, CoordTranslation.MINUS_X, faces[3], faces[5])),
        Adjacency(faces[4], Heading.DOWN, flatTranslation(Heading.DOWN)),
        Adjacency(faces[2], Heading.LEFT, flatTranslation(Heading.LEFT)),
        Adjacency(faces[0], Heading.UP, flatTranslation(Heading.UP))
      )
      faceAdjacencyMap[faces[4]] = AdjacentFaces(
        Adjacency(faces[5], Heading.RIGHT, flatTranslation(Heading.RIGHT)),
        Adjacency(faces[1], Heading.UP, tranlation(CoordTranslation.MAX, CoordTranslation.MINUS_Y, faces[4], faces[1])),
        Adjacency(faces[2], Heading.UP, tranlation(CoordTranslation.MAX, CoordTranslation.MINUS_X, faces[4], faces[2])),
        Adjacency(faces[3], Heading.UP, flatTranslation(Heading.UP))
      )
      faceAdjacencyMap[faces[5]] = AdjacentFaces(
        Adjacency(faces[0], Heading.LEFT, tranlation(CoordTranslation.MINUS_X, CoordTranslation.MAX, faces[5], faces[0])),
        Adjacency(faces[1], Heading.LEFT, tranlation(CoordTranslation.MINUS_Y, CoordTranslation.MAX, faces[5], faces[1])),
        Adjacency(faces[4], Heading.LEFT, flatTranslation(Heading.LEFT)),
        Adjacency(faces[3], Heading.LEFT, tranlation(CoordTranslation.MINUS_Y, CoordTranslation.MAX, faces[5], faces[3]))
      )
    }

    private fun Pair<Int, Int>.wrap(): Pair<Int, Int> {
      var (x, y) = this
      if (x < 0) x = maxX
      if (x > maxX) x = 0
      if (y < 0) y = maxY
      if (y > maxY) y = 0
      return Pair(x, y)
    }

    fun step(coord: Pair<Int, Int>, heading: Heading, steps: Int): Pair<Int, Int> {
      var coord = coord

      for (i in 1..steps) {
        val preStep = coord
        do {
          coord = heading.step(coord).wrap()
        } while (get(coord.first, coord.second) == Tile.BLANK)
        if (get(coord.first, coord.second) == Tile.WALL) return preStep
      }
      return coord
    }

    fun cubeStep(coord: Pair<Int, Int>, heading: Heading, steps: Int): Pair<Int, Int> {
      var coord = coord

      for (i in 1..steps) {
        val preStep = coord
        val sourceFace = coordFaceMap[coord.first]!!.get(coord.second)!!
        coord = heading.step(coord)
        //if (sourceFace != coordFaceMap[coord.first]?.get(coord.second)) {
        if (coordFaceMap[coord.first]?.get(coord.second) == null) {
          coord = when (heading) {
            // walked off the side of the map, need to translate
            Heading.RIGHT -> faceAdjacencyMap[sourceFace]!!.right.translation.invoke(preStep)
            Heading.DOWN -> faceAdjacencyMap[sourceFace]!!.down.translation.invoke(preStep)
            Heading.LEFT -> faceAdjacencyMap[sourceFace]!!.left.translation.invoke(preStep)
            Heading.UP -> faceAdjacencyMap[sourceFace]!!.up.translation.invoke(preStep)
          }
        }

        if (get(coord.first, coord.second) == Tile.WALL) return preStep
      }
      return coord
    }
  }

  val stepInstructionRegex = Regex("[0-9]+")
  val turnInstructionRegex = Regex("[LR]")
  fun parseInput(input: String): Pair<Grid, List<Instruction>> {
    val grid = Grid(faceSize)
    val instructions = ArrayList<Instruction>()
    var inGrid = true
    for ((row, line) in input.lines().withIndex()) {
      if (inGrid) {
        if (line.isBlank()) {
          inGrid = false
          continue
        }

        for ((col, c) in line.withIndex()) {
          grid.put(row, col, when(c) {
            ' ' -> Tile.BLANK
            '.' -> Tile.AIR
            '#' -> Tile.WALL
            else -> throw Exception()
          })
        }
      } else {
        var i = 0
        while (i < line.length) {
          var match = turnInstructionRegex.matchAt(line, i)
          if (match == null) {
            match = stepInstructionRegex.matchAt(line, i)
            instructions.add(StepInstruction(
              match!!.value.toInt()
            ))
          } else {
            instructions.add(TurnInstruction(
              Turn.valueOf(match.value)
            ))
          }
          i += match.value.length
        }
      }
    }

    return Pair(grid, instructions)
  }

  override fun part1(input: String): Any? {
    val (grid, instructions) = parseInput(input)

    var coord = Pair(0, grid.minRow0)
    var heading = Heading.RIGHT

    for (instruction in instructions) {
      when (instruction) {
        is TurnInstruction -> heading = heading.turn(instruction.turn)
        is StepInstruction -> coord = grid.step (coord, heading, instruction.steps)
      }
    }

    return 1000 * (coord.first + 1) + 4 * (coord.second + 1) + heading.ordinal
  }

  override fun part2(input: String): Any? {
    val (grid, instructions) = parseInput(input)
    grid.identifyFaces()

    var coord = Pair(0, grid.minRow0)
    var heading = Heading.RIGHT

    for (instruction in instructions) {
      when (instruction) {
        is TurnInstruction -> heading = heading.turn(instruction.turn)
        is StepInstruction -> coord = grid.cubeStep (coord, heading, instruction.steps)
      }
    }

    return 1000 * (coord.first + 1) + 4 * (coord.second + 1) + heading.ordinal
  }
}