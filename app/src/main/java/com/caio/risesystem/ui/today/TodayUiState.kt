package com.caio.risesystem.ui.today

import com.caio.risesystem.domain.model.DailyTask

data class TodayUiState(
    val dateDisplay: String = "",
    val tasks: List<DailyTask> = emptyList(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val percentage: Int = 0,
    val classification: String = "Dia não cumprido",
    val isLoading: Boolean = false
)
