package devson.timefreezer

import java.time.LocalDate
import java.time.LocalDateTime

private val freezer = TimeFreezer()

fun freezeTime(date: LocalDate, block: UnitBlock) = freezer.freeze(FreezingTime.LocalDate(date), block)

fun freezeTime(dateTime: LocalDateTime, block: UnitBlock) = freezer.freeze(FreezingTime.LocalDateTime(dateTime), block)
