package com.caio.risesystem.domain.model

enum class DailyTaskStatus {
    PENDING,
    COMPLETED,
    NOT_COMPLETED;

    fun toReadableString(): String = when (this) {
        PENDING -> "Pendente"
        COMPLETED -> "Concluída"
        NOT_COMPLETED -> "Não realizada"
    }
}
