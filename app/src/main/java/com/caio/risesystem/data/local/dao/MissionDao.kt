package com.caio.risesystem.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caio.risesystem.data.local.entity.MissionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MissionDao {

    @Query("SELECT * FROM missions ORDER BY id ASC")
    fun observeAllMissions(): Flow<List<MissionEntity>>

    @Query("SELECT * FROM missions WHERE id = :missionId LIMIT 1")
    suspend fun getMissionById(
        missionId: Int
    ): MissionEntity?

    @Query("SELECT COUNT(*) FROM missions")
    suspend fun countMissions(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        missions: List<MissionEntity>
    )

    @Query(
        """
        UPDATE missions
        SET status = :newStatus
        WHERE id = :missionId
        """
    )
    suspend fun updateStatus(
        missionId: Int,
        newStatus: String
    )
}
