package com.caio.risesystem.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(
        database: SupportSQLiteDatabase
    ) {
        database.execSQL(
            """
            ALTER TABLE missions
            ADD COLUMN validationType TEXT
            NOT NULL DEFAULT 'MANUAL'
            """.trimIndent()
        )

        database.execSQL(
            """
            ALTER TABLE missions
            ADD COLUMN targetValue INTEGER
            NOT NULL DEFAULT 1
            """.trimIndent()
        )

        database.execSQL(
            """
            ALTER TABLE missions
            ADD COLUMN targetUnit TEXT
            NOT NULL DEFAULT 'tarefa'
            """.trimIndent()
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `daily_tasks` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `date` TEXT NOT NULL, 
                `type` TEXT NOT NULL, 
                `title` TEXT NOT NULL, 
                `summary` TEXT NOT NULL, 
                `suggestedTime` TEXT NOT NULL, 
                `status` TEXT NOT NULL, 
                `sortOrder` INTEGER NOT NULL, 
                `details` TEXT NOT NULL, 
                `createdAt` INTEGER NOT NULL, 
                `updatedAt` INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}
