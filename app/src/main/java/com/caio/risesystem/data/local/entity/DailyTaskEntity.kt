package com.caio.risesystem.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_tasks")
data class DailyTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val type: String,
    val title: String,
    val summary: String,
    val suggestedTime: String,
    val status: String,
    val sortOrder: Int,
    val details: String,
    val createdAt: Long,
    val updatedAt: Long
)
