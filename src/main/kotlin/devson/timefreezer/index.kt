package devson.timefreezer

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

private val mockDates: Stack<LocalDate> = Stack()

fun freeze(mockDate: LocalDate, block: () -> Unit) {
    mockDates.push(mockDate)
    mockTime(mockDate)

    try {
        block.invoke()
    } finally {
        /****************************************
         * DO NOT USE `return` TO REDUCE INDENT *
         ****************************************/
        mockDates.pop()

        // when there's no nested mocking
        if (mockDates.isEmpty()) {
            unmockTime()
        } else {
            // when there's nested mocking
            val previousMockDate: LocalDate = mockDates.peek()
            mockTime(previousMockDate)
        }
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
