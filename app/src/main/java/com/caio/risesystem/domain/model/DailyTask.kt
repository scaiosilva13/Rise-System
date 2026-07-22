package com.caio.risesystem.domain.model

data class DailyTask(
    val id: Long,
    val date: String,
    val type: DailyTaskType,
    val title: String,
    val summary: String,
    val suggestedTime: String,
    val status: DailyTaskStatus,
    val sortOrder: Int,
    val details: String = ""
)
