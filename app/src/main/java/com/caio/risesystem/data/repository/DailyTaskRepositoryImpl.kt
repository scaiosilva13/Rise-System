package com.caio.risesystem.data.repository

import com.caio.risesystem.data.local.RiseDatabase
import com.caio.risesystem.data.local.entity.DailyTaskEntity
import com.caio.risesystem.data.mapper.toDomain
import com.caio.risesystem.domain.model.DailyTask
import com.caio.risesystem.domain.model.DailyTaskStatus
import com.caio.risesystem.domain.model.DailyTaskType
import com.caio.risesystem.domain.repository.DailyTaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DailyTaskRepositoryImpl(
    database: RiseDatabase
) : DailyTaskRepository {

    private val dailyTaskDao = database.dailyTaskDao()

    override fun observeTasksByDate(date: String): Flow<List<DailyTask>> {
        return dailyTaskDao.observeByDate(date).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateTaskStatus(id: Long, status: DailyTaskStatus) {
        dailyTaskDao.updateStatus(id, status.name, System.currentTimeMillis())
    }

    override suspend fun initializeTasksForDate(date: String) {
        val now = System.currentTimeMillis()
        val initialTasks = listOf(
            createDemoTask(date, DailyTaskType.WATER, "Água ao acordar", "07:00", 1, now),
            createDemoTask(date, DailyTaskType.MEAL, "Café da manhã", "07:30", 2, now),
            createDemoTask(date, DailyTaskType.MEAL, "Lanche da manhã", "10:00", 3, now),
            createDemoTask(date, DailyTaskType.MEAL, "Almoço", "12:30", 4, now),
            createDemoTask(date, DailyTaskType.MEAL, "Lanche da tarde", "17:30", 5, now),
            createDemoTask(date, DailyTaskType.WORKOUT, "Treino do dia", "19:00", 6, now),
            createDemoTask(date, DailyTaskType.MEAL, "Jantar", "20:30", 7, now),
            createDemoTask(date, DailyTaskType.WATER, "Meta de água", "22:00", 8, now),
            createDemoTask(date, DailyTaskType.SLEEP, "Meta de sono", "23:30", 9, now)
        )
        dailyTaskDao.initializeTasksIfEmpty(date, initialTasks)
    }

    private fun createDemoTask(
        date: String,
        type: DailyTaskType,
        title: String,
        time: String,
        order: Int,
        timestamp: Long
    ) = DailyTaskEntity(
        date = date,
        type = type.name,
        title = title,
        summary = "",
        suggestedTime = time,
        status = DailyTaskStatus.PENDING.name,
        sortOrder = order,
        details = "",
        createdAt = timestamp,
        updatedAt = timestamp
    )
}
