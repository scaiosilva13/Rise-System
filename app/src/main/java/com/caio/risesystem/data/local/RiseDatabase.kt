package com.caio.risesystem.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.caio.risesystem.data.local.dao.MissionDao
import com.caio.risesystem.data.local.dao.PlayerDao
import com.caio.risesystem.data.local.entity.MissionEntity
import com.caio.risesystem.data.local.entity.PlayerEntity

@Database(
    entities = [
        MissionEntity::class,
        PlayerEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class RiseDatabase : RoomDatabase() {

    abstract fun missionDao(): MissionDao

    abstract fun playerDao(): PlayerDao
}
