package com.caio.risesystem

import android.app.Application
import androidx.room.Room
import com.caio.risesystem.data.local.RiseDatabase
import com.caio.risesystem.data.repository.RiseRepository
import com.caio.risesystem.data.local.MIGRATION_1_2
import com.caio.risesystem.data.local.MIGRATION_2_3

class RiseApplication : Application() {

    val database: RiseDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            RiseDatabase::class.java,
            "rise_system_database"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    val repository: RiseRepository by lazy {
        RiseRepository(database)
    }
}
