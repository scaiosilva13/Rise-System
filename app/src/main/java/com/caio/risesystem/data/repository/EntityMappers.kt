package com.caio.risesystem.data.repository

import com.caio.risesystem.data.local.entity.MissionEntity
import com.caio.risesystem.data.local.entity.PlayerEntity
import com.caio.risesystem.domain.model.Mission
import com.caio.risesystem.domain.model.MissionCategory
import com.caio.risesystem.domain.model.MissionStatus
import com.caio.risesystem.domain.model.Player
import com.caio.risesystem.domain.model.ValidationType

fun MissionEntity.toDomain(): Mission {
    return Mission(
        id = id,
        title = title,
        description = description,
        category = MissionCategory.valueOf(category),
        xpReward = xpReward,
        status = MissionStatus.valueOf(status),
        validationType =
            ValidationType.valueOf(validationType),
        targetValue = targetValue,
        targetUnit = targetUnit
    )
}

fun Mission.toEntity(): MissionEntity {
    return MissionEntity(
        id = id,
        title = title,
        description = description,
        category = category.name,
        xpReward = xpReward,
        status = status.name,
        validationType = validationType.name,
        targetValue = targetValue,
        targetUnit = targetUnit
    )
}

fun PlayerEntity.toDomain(): Player {
    return Player(
        name = name,
        level = level,
        rank = rank,
        currentXp = currentXp,
        xpToNextLevel = xpToNextLevel,
        totalXp = totalXp,
        currentStreak = currentStreak
    )
}

fun Player.toEntity(): PlayerEntity {
    return PlayerEntity(
        id = 1,
        name = name,
        level = level,
        rank = rank,
        currentXp = currentXp,
        xpToNextLevel = xpToNextLevel,
        totalXp = totalXp,
        currentStreak = currentStreak
    )
}

