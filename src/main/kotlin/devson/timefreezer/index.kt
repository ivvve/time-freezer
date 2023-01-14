package devson.timefreezer

import java.time.LocalDate
import java.time.LocalDateTime

private val freezer = TimeFreezer()

fun freeze(date: LocalDate, block: UnitBlock) = freezer.freeze(FreezingTime.LocalDate(date), block)

fun freeze(dateTime: LocalDateTime, block: UnitBlock) = freezer.freeze(FreezingTime.LocalDateTime(dateTime), block)
