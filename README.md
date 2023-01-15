# TimeFreezer: Kotlin time mocking library inspired by [FreezeGun](https://github.com/spulec/freezegun)

![ci](https://github.com/ivvve/time-freezer/actions/workflows/ci.yml/badge.svg)

TimeFreezer makes you test time related code with `LocalDate.now()` / `LocalDateTime.now()`
based on [MockK](https://mockk.io/) the mocking library for Kotlin.

## Usage

### Adding dependency

This library is published by [JitPack](https://jitpack.io/), so you have to add JitPack Maven repository.

```kt
// build.gradle.kt

repositories {
    ...
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    ...
    testImplementation("com.github.ivvve:time-freezer:${TimeFreezerVersion}")
}
```

### Simple usage

You can fix the time using `devson.timefreezer.freezeTime` function.  
See the example code below.

```kotlin
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
```

### Nested usage

You can also use `devson.timefreezer.freezeTime` function nested form.

```kotlin
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
```

## Caution

On JDK 16 and above you can run into `IllegalAccessException` / `InaccessibleObjectException`
when you use `LocalDate` or `LocalDateTime` methods like `LocalDate.of(2023, 1, 15)`.  
It's because of strong module encapsulation of JDK 16 and because TimeFreeze is based on MockK.

You can bypass this issue adding JVM argument `--add-opens java.base/java.time=ALL-UNNAMED`.

Example for Gradle users:

```kt
// build.gradle.kt

tasks.test {
    useJUnitPlatform()

    // to resolve mockk issue caused by Java module system (reference: https://github.com/mockk/mockk/blob/master/doc/md/jdk16-access-exceptions.md)
    jvmArgs(
        "--add-opens", "java.base/java.time=ALL-UNNAMED",
    )
}
```
