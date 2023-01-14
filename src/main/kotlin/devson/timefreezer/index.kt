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

fun freeze(date: LocalDate, block: UnitBlock) = freeze(FreezingTime.LocalDate(date), block)

fun freeze(dateTime: LocalDateTime, block: UnitBlock) = freeze(FreezingTime.LocalDateTime(dateTime), block)

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
        is FreezingTime.LocalDate -> {
            val date = time.value

            mockkStatic(LocalDate::class)
            every { LocalDate.now() } returns date

            mockkStatic(LocalDateTime::class)
            every { LocalDateTime.now() } returns LocalDateTime.of(date, LocalTime.MIN)
        }

        is FreezingTime.LocalDateTime -> {
            val dateTime = time.value

            mockkStatic(LocalDate::class)
            every { LocalDate.now() } returns dateTime.toLocalDate()

            mockkStatic(LocalDateTime::class)
            every { LocalDateTime.now() } returns dateTime
        }
    }

}

private fun unmockTime() {
    unmockkStatic(
        LocalDate::class,
        LocalDateTime::class,
    )
}
