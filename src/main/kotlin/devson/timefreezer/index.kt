package devson.timefreezer

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun freeze(date: LocalDate, block: () -> Unit) {
    mockkStatic(LocalDate::class)
    every { LocalDate.now() } returns date

    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns LocalDateTime.of(date, LocalTime.MIN)

    try {
        block.invoke()
    } finally {
        unmockkStatic(
            LocalDate::class,
            LocalDateTime::class,
        )
    }
}
