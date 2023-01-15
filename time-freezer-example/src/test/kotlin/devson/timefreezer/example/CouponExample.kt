package devson.timefreezer.example

import devson.timefreezer.freezeTime
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.LocalDate
import java.time.LocalDateTime

internal class CouponExample : FreeSpec({
    "Coupon example" {
        val sut = Coupon(
            canUseFrom = LocalDate.of(2023, 1, 1),
            expiresAt = LocalDateTime.of(2023, 1, 3, 22, 0, 0),
        )

        // before date can use the coupon
        freezeTime(LocalDate.of(2022, 12, 31)) {
            sut.isAvailable().shouldBeFalse()
            sut.isAfterCanUseFrom().shouldBeFalse()
            sut.isExpired().shouldBeFalse()
        }

        // during date can use the coupon
        freezeTime(LocalDateTime.of(2023, 1, 1, 0, 0, 0)) {
            sut.isAvailable().shouldBeTrue()
            sut.isAfterCanUseFrom().shouldBeTrue()
            sut.isExpired().shouldBeFalse()
        }

        // after coupon expired
        freezeTime(LocalDateTime.of(2023, 1, 3, 23, 0, 1)) {
            sut.isAvailable().shouldBeFalse()
            sut.isAfterCanUseFrom().shouldBeTrue()
            sut.isExpired().shouldBeTrue()
        }
    }
})

private data class Coupon(
    val canUseFrom: LocalDate,
    val expiresAt: LocalDateTime,
) {
    fun isAvailable(): Boolean = (this.isAfterCanUseFrom()) && this.isExpired().not()

    fun isAfterCanUseFrom(): Boolean = (this.canUseFrom <= LocalDate.now())

    fun isExpired(): Boolean = (LocalDateTime.now() > this.expiresAt)
}
