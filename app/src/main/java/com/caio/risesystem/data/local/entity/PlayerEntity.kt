package com.caio.risesystem.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class PlayerEntity(
    @PrimaryKey
    val id: Int = 1,

    val name: String,
    val level: Int,
    val rank: String,
    val currentXp: Int,
    val xpToNextLevel: Int,
    val totalXp: Int,
    val currentStreak: Int
)

