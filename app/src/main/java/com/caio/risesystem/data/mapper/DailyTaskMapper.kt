package com.caio.risesystem.data.mapper

import com.caio.risesystem.data.local.entity.DailyTaskEntity
import com.caio.risesystem.domain.model.DailyTask
import com.caio.risesystem.domain.model.DailyTaskStatus
import com.caio.risesystem.domain.model.DailyTaskType

fun DailyTaskEntity.toDomain(): DailyTask = DailyTask(
    id = id,
    date = date,
    type = DailyTaskType.valueOf(type),
    title = title,
    summary = summary,
    suggestedTime = suggestedTime,
    status = DailyTaskStatus.valueOf(status),
    sortOrder = sortOrder,
    details = details
)

fun DailyTask.toEntity(createdAt: Long, updatedAt: Long): DailyTaskEntity = DailyTaskEntity(
    id = id,
    date = date,
    type = type.name,
    title = title,
    summary = summary,
    suggestedTime = suggestedTime,
    status = status.name,
    sortOrder = sortOrder,
    details = details,
    createdAt = createdAt,
    updatedAt = updatedAt
)
