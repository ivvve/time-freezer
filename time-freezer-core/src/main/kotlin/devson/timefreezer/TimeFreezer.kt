package devson.timefreezer

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

internal typealias UnitBlock = () -> Unit

internal class TimeFreezer {
    private val freezingTimes = Stack<FreezingTime<*, *>>()

    fun freeze(time: FreezingTime<*, *>, block: UnitBlock) {
        this.freezingTimes.push(time)
        mockTime(time)

        try {
            block.invoke()
        } finally {
            /****************************************
             * DO NOT USE `return` TO REDUCE INDENT *
             ****************************************/
            this.freezingTimes.pop()

            when (this.freezingTimes.isEmpty()) {
                // when there's no nested mocking
                true -> {
                    this.unmockTime()
                }

                // when there's nested mocking
                false -> {
                    val previousFreezingTime = freezingTimes.peek()
                    this.mockTime(previousFreezingTime)
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
}
