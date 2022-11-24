package com.staricka.adventofcode2022

import kotlin.reflect.full.createInstance

fun main(args: Array<String>) {
  println("Run which day?")

  // TODO: part selection, input bypass?, env variable for quick execution?
  val input = readln().toIntOrNull()
  if (input == null) {
    println("Invalid day")
    return
  }

  val day = Class.forName("com.staricka.adventofcode2022.Day$input")?.kotlin
  if (day == null) {
    println("Invalid day")
    return
  }

  (day.createInstance() as Day).run(DayPart.BOTH)
}