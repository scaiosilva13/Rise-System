package com.caio.risesystem.domain.model

enum class DailyTaskType {
    MEAL,
    WORKOUT,
    CARDIO,
    WATER,
    STEPS,
    SLEEP,
    WEIGH_IN,
    OTHER;

    fun toReadableString(): String = when (this) {
        MEAL -> "Alimentação"
        WORKOUT -> "Treino"
        CARDIO -> "Cardio"
        WATER -> "Água"
        STEPS -> "Passos"
        SLEEP -> "Sono"
        WEIGH_IN -> "Pesagem"
        OTHER -> "Outro"
    }
}
