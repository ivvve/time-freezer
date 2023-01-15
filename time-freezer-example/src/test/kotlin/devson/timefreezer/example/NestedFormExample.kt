package devson.timefreezer.example

import devson.timefreezer.freezeTime
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

internal class NestedFormExample : FreeSpec({
    "Nested form example" {
        freezeTime(LocalDateTime.of(2023, 1, 2, 3, 4, 5)) {
            LocalDate.now().shouldBe(LocalDate.of(2023, 1, 2))
            LocalDateTime.now().shouldBe(LocalDateTime.of(2023, 1, 2, 3, 4, 5))

            freezeTime(LocalDateTime.of(2023, 2, 3, 4, 5, 6)) {
                LocalDate.now().shouldBe(LocalDate.of(2023, 2, 3))
                LocalDateTime.now().shouldBe(LocalDateTime.of(2023, 2, 3, 4, 5, 6))
            }

            LocalDate.now().shouldBe(LocalDate.of(2023, 1, 2))
            LocalDateTime.now().shouldBe(LocalDateTime.of(2023, 1, 2, 3, 4, 5))
        }
    }
})
