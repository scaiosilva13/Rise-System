package com.caio.risesystem.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missions")
data class MissionEntity(
    @PrimaryKey
    val id: Int,

    val title: String,
    val description: String,
    val category: String,
    val xpReward: Int,
    val status: String,

    val validationType: String,
    val targetValue: Int,
    val targetUnit: String
)
