package devson.timefreezer

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

private val freezingTimes = Stack<FreezingTime<*, *>>()

private typealias UnitBlock = () -> Unit

fun freeze(mockDate: LocalDate, block: UnitBlock) = freeze(FreezingTime.LocalDate(mockDate), block)

private fun freeze(time: FreezingTime<*, *>, block: UnitBlock) {
    freezingTimes.push(time)
    mockTime(time)

    try {
        block.invoke()
    } finally {
        /****************************************
         * DO NOT USE `return` TO REDUCE INDENT *
         ****************************************/
        freezingTimes.pop()

        when (freezingTimes.isEmpty()) {
            // when there's no nested mocking
            true -> {
                unmockTime()
            }

            // when there's nested mocking
            false -> {
                val previousFreezingTime = freezingTimes.peek()
                mockTime(previousFreezingTime)
            }
        }
    }
}

private fun mockTime(time: FreezingTime<*, *>) {
    when (time) {
        is FreezingTime.LocalDate -> mockTime(time.value)
        is FreezingTime.LocalDateTime -> TODO()
    }
}

private fun mockTime(date: LocalDate) {
    mockkStatic(LocalDate::class)
    every { LocalDate.now() } returns date

    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns LocalDateTime.of(date, LocalTime.MIN)
}

private fun unmockTime() {
    unmockkStatic(
        LocalDate::class,
        LocalDateTime::class,
    )
}
