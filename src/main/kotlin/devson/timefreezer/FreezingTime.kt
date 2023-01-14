package devson.timefreezer

sealed class FreezingTime<DATE, DATE_TIME> {

    open val isLocalDate: Boolean = false
    open val isLocalDateTime: Boolean = false

    data class LocalDate(
        val value: java.time.LocalDate
    ) : FreezingTime<java.time.LocalDate, Nothing>() {

        override val isLocalDate: Boolean = true
    }

    data class LocalDateTime(
        val value: java.time.LocalDateTime
    ) : FreezingTime<Nothing, java.time.LocalDateTime>() {

        override val isLocalDateTime: Boolean = true
    }
}
