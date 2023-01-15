package devson.timefreezer

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class FreezeTest : FreeSpec({
    "freeze LocalDate" - {
        "single block" {
            lateinit var `LocalDate#now`: LocalDate
            lateinit var `LocalDateTime#now`: LocalDateTime

            freezeTime(LocalDate.of(2022, 1, 12)) {
                `LocalDate#now` = LocalDate.now()
                `LocalDateTime#now` = LocalDateTime.now()
            }

            `LocalDate#now`.shouldBe(LocalDate.of(2022, 1, 12))
            `LocalDateTime#now`.shouldBe(LocalDateTime.of(2022, 1, 12, 0, 0, 0))
        }

        "nested blocks" {
            lateinit var `LocalDate#now - 1 depth`: LocalDate
            lateinit var `LocalDateTime#now - 1 depth`: LocalDateTime
            lateinit var `LocalDate#now - 2 depth`: LocalDate
            lateinit var `LocalDateTime#now - 2 depth`: LocalDateTime
            lateinit var `LocalDate#now - 3 depth`: LocalDate
            lateinit var `LocalDateTime#now - 3 depth`: LocalDateTime


            freezeTime(LocalDate.of(2022, 1, 12)) {
                freezeTime(LocalDate.of(2022, 1, 8)) {
                    `LocalDate#now - 2 depth` = LocalDate.now()
                    `LocalDateTime#now - 2 depth` = LocalDateTime.now()

                    freezeTime(LocalDate.of(2022, 1, 13)) {
                        `LocalDate#now - 3 depth` = LocalDate.now()
                        `LocalDateTime#now - 3 depth` = LocalDateTime.now()
                    }
                }

                `LocalDate#now - 1 depth` = LocalDate.now()
                `LocalDateTime#now - 1 depth` = LocalDateTime.now()
            }

            `LocalDate#now - 1 depth`.shouldBe(LocalDate.of(2022, 1, 12))
            `LocalDateTime#now - 1 depth`.shouldBe(LocalDateTime.of(2022, 1, 12, 0, 0, 0))
            `LocalDate#now - 2 depth`.shouldBe(LocalDate.of(2022, 1, 8))
            `LocalDateTime#now - 2 depth`.shouldBe(LocalDateTime.of(2022, 1, 8, 0, 0, 0))
            `LocalDate#now - 3 depth`.shouldBe(LocalDate.of(2022, 1, 13))
            `LocalDateTime#now - 3 depth`.shouldBe(LocalDateTime.of(2022, 1, 13, 0, 0, 0))
        }
    }

    "freeze LocalDateTime" - {
        "single block" {
            lateinit var `LocalDate#now`: LocalDate
            lateinit var `LocalDateTime#now`: LocalDateTime

            freezeTime(LocalDateTime.of(2022, 1, 12, 1, 2, 3)) {
                `LocalDate#now` = LocalDate.now()
                `LocalDateTime#now` = LocalDateTime.now()
            }

            `LocalDate#now`.shouldBe(LocalDate.of(2022, 1, 12))
            `LocalDateTime#now`.shouldBe(LocalDateTime.of(2022, 1, 12, 1, 2, 3))
        }

        "nested blocks" {
            lateinit var `LocalDate#now - 1 depth`: LocalDate
            lateinit var `LocalDateTime#now - 1 depth`: LocalDateTime
            lateinit var `LocalDate#now - 2 depth`: LocalDate
            lateinit var `LocalDateTime#now - 2 depth`: LocalDateTime
            lateinit var `LocalDate#now - 3 depth`: LocalDate
            lateinit var `LocalDateTime#now - 3 depth`: LocalDateTime


            freezeTime(LocalDateTime.of(2022, 1, 12, 1, 2, 3)) {
                freezeTime(LocalDateTime.of(2022, 1, 8, 2, 3, 4)) {
                    `LocalDate#now - 2 depth` = LocalDate.now()
                    `LocalDateTime#now - 2 depth` = LocalDateTime.now()

                    freezeTime(LocalDateTime.of(2022, 1, 13, 3, 4, 5)) {
                        `LocalDate#now - 3 depth` = LocalDate.now()
                        `LocalDateTime#now - 3 depth` = LocalDateTime.now()
                    }
                }

                `LocalDate#now - 1 depth` = LocalDate.now()
                `LocalDateTime#now - 1 depth` = LocalDateTime.now()
            }

            `LocalDate#now - 1 depth`.shouldBe(LocalDate.of(2022, 1, 12))
            `LocalDateTime#now - 1 depth`.shouldBe(LocalDateTime.of(2022, 1, 12, 1, 2, 3))
            `LocalDate#now - 2 depth`.shouldBe(LocalDate.of(2022, 1, 8))
            `LocalDateTime#now - 2 depth`.shouldBe(LocalDateTime.of(2022, 1, 8, 2, 3, 4))
            `LocalDate#now - 3 depth`.shouldBe(LocalDate.of(2022, 1, 13))
            `LocalDateTime#now - 3 depth`.shouldBe(LocalDateTime.of(2022, 1, 13, 3, 4, 5))
        }
    }
})
