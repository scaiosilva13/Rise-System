package com.caio.risesystem.domain.repository

import com.caio.risesystem.domain.model.DailyTask
import com.caio.risesystem.domain.model.DailyTaskStatus
import kotlinx.coroutines.flow.Flow

interface DailyTaskRepository {
    fun observeTasksByDate(date: String): Flow<List<DailyTask>>
    fun observeTaskById(id: Long): Flow<DailyTask?>
    suspend fun updateTaskStatus(id: Long, status: DailyTaskStatus)
    suspend fun initializeTasksForDate(date: String)
}
