package devson.timefreezer

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class TimeFreezerTest : FreeSpec({
    "freeze LocalDate" - {
        "single block" {
            lateinit var `LocalDate#now`: LocalDate
            lateinit var `LocalDateTime#now`: LocalDateTime

            freeze(LocalDate.of(2022, 1, 12)) {
                `LocalDate#now` = LocalDate.now()
                `LocalDateTime#now` = LocalDateTime.now()
            }

            `LocalDate#now`.shouldBe(LocalDate.of(2022, 1, 12))
            `LocalDateTime#now`.shouldBe(LocalDateTime.of(2022, 1, 12, 0, 0, 0))
        }
    }
})
