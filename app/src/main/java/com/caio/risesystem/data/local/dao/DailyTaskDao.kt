package com.caio.risesystem.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.caio.risesystem.data.local.entity.DailyTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DailyTaskDao {

    @Query("SELECT * FROM daily_tasks WHERE date = :date ORDER BY sortOrder ASC")
    abstract fun observeByDate(date: String): Flow<List<DailyTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(tasks: List<DailyTaskEntity>)

    @Query("UPDATE daily_tasks SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    abstract suspend fun updateStatus(id: Long, status: String, updatedAt: Long)

    @Query("SELECT COUNT(*) FROM daily_tasks WHERE date = :date")
    abstract suspend fun countByDate(date: String): Int

    @Transaction
    open suspend fun initializeTasksIfEmpty(date: String, tasks: List<DailyTaskEntity>) {
        val count = countByDate(date)
        if (count == 0) {
            insertAll(tasks)
        }
    }
}
