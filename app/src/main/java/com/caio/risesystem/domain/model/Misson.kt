package com.caio.risesystem.domain.model

enum class MissionStatus {
    PENDING,
    ACTIVE,
    COMPLETED,
    FAILED
}

enum class MissionCategory {
    CARDIO,
    STRENGTH,
    MOBILITY,
    MENTAL
}

data class Mission(
    val id: Int,
    val title: String,
    val description: String,
    val category: MissionCategory,
    val xpReward: Int,
    val status: MissionStatus = MissionStatus.PENDING,
    val validationType: ValidationType,
    val targetValue: Int,
    val targetUnit: String
)

