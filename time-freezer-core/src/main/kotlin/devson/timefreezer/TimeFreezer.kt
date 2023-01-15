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
    private val frozenTimes = Stack<FrozenTime<*, *>>()

    fun freeze(time: FrozenTime<*, *>, block: UnitBlock) {
        this.frozenTimes.push(time)
        mockTime(time)

        try {
            block.invoke()
        } finally {
            /****************************************
             * DO NOT USE `return` TO REDUCE INDENT *
             ****************************************/
            this.frozenTimes.pop()

            when (this.frozenTimes.isEmpty()) {
                // when there's no nested mocking
                true -> {
                    this.unmockTime()
                }

                // when there's nested mocking
                false -> {
                    val previousFreezingTime = frozenTimes.peek()
                    this.mockTime(previousFreezingTime)
                }
            }
        }
    }

    private fun mockTime(time: FrozenTime<*, *>) {
        when (time) {
            is FrozenTime.LocalDate -> {
                val date = time.value

                mockkStatic(LocalDate::class)
                every { LocalDate.now() } returns date

                mockkStatic(LocalDateTime::class)
                every { LocalDateTime.now() } returns LocalDateTime.of(date, LocalTime.MIN)
            }

            is FrozenTime.LocalDateTime -> {
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
