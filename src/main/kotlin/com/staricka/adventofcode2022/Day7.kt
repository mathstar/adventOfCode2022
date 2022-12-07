package com.staricka.adventofcode2022

import kotlin.math.min

class Day7 : Day {
  override val id = 7

  enum class FileType {
    DIRECTORY, REGULAR_FILE
  }

  interface File {
    val fileType: FileType
    val name: String
    val size: Int
  }

  data class Directory(override val name: String, val parent: Directory? = null) : File {
    override val fileType = FileType.DIRECTORY

    val children = HashMap<String, File>()

    override val size: Int
      get() = children.values.sumOf { it.size }
  }

  data class RegularFile(override val name: String, override val size: Int) : File {
    override val fileType = FileType.REGULAR_FILE
  }

  private val rootCdRegex = Regex("""\$ cd /""")
  private val upCdRegex = Regex("""\$ cd \.\.""")
  private val cdRegex = Regex("""\$ cd (.+)""")
  private val lsRegex = Regex("""\$ ls""")
  private val dirRegex = Regex("dir (.+)")
  private val fileRegex = Regex("([0-9]+) (.+)")

  private fun parseFileSystem(input: String): Directory {
    val root = Directory("/")
    var pwd = root
    input.lines().forEach {line ->
      if (rootCdRegex.matches(line)) {
        pwd = root
      } else if (upCdRegex.matches(line)) {
        pwd = pwd.parent!!
      } else if (cdRegex.matches(line)) {
        pwd = pwd.children[cdRegex.matchEntire(line)!!.groups[1]!!.value] as Directory
      } else if (lsRegex.matches(line)) {
        // noop
      } else if (dirRegex.matches(line)) {
        val match = dirRegex.matchEntire(line)
        pwd.children[match!!.groups[1]!!.value] = Directory(match.groups[1]!!.value, pwd)
      } else if (fileRegex.matches(line)) {
        val match = fileRegex.matchEntire(line)
        pwd.children[match!!.groups[2]!!.value] = RegularFile(match.groups[2]!!.value, match.groups[1]!!.value.toInt())
      }
    }
    return root
  }

  private fun walkFileSystemDirectories(root: Directory, action: (Directory) -> Unit) {
    action(root)
    for (file in root.children.values) {
      if (file.fileType == FileType.DIRECTORY) {
        walkFileSystemDirectories(file as Directory, action)
      }
    }
  }

  override fun part1(input: String): Any {
    val fileSystem = parseFileSystem(input)
    var sum = 0
    walkFileSystemDirectories(fileSystem) {
      if (it.size <= 100000) sum += it.size
    }
    return sum
  }

  override fun part2(input: String): Any {
    val fileSystem = parseFileSystem(input)
    var candidateSize = Int.MAX_VALUE
    val neededSpace = 30000000 - (70000000 - fileSystem.size)
    walkFileSystemDirectories(fileSystem) {
      if (it.size >= neededSpace) candidateSize = min(candidateSize, it.size)
    }
    return candidateSize
  }
}