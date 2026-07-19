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