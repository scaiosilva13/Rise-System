package com.caio.risesystem.domain.adherence

data class DailyAdherenceResult(
    val completedCount: Int,
    val totalCount: Int,
    val percentage: Int,
    val classification: String
)
