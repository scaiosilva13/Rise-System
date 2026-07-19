package com.caio.risesystem.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caio.risesystem.data.local.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Query("SELECT * FROM player WHERE id = 1 LIMIT 1")
    fun observePlayer(): Flow<PlayerEntity?>

    @Query("SELECT * FROM player WHERE id = 1 LIMIT 1")
    suspend fun getPlayer(): PlayerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlayer(
        player: PlayerEntity
    )
}


