package devson.timefreezer

internal sealed class FrozenTime {
    open val isLocalDate: Boolean = false
    open val isLocalDateTime: Boolean = false

    internal data class LocalDate(
        val value: java.time.LocalDate
    ) : FrozenTime() {

        override val isLocalDate: Boolean = true
    }

    internal data class LocalDateTime(
        val value: java.time.LocalDateTime
    ) : FrozenTime() {

        override val isLocalDateTime: Boolean = true
    }
}
