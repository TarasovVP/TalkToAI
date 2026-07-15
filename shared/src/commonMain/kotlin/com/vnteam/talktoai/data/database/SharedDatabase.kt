package com.vnteam.talktoai.data.database

import com.vnteam.talktoai.AppDatabase
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SharedDatabase(private val databaseDriverFactory: DatabaseDriverFactory) {
    private var database: AppDatabase? = null
    private val initMutex = Mutex()

    private suspend fun initDatabase() {
        if (database != null) return
        initMutex.withLock {
            if (database != null) return
            database = AppDatabase(databaseDriverFactory.createDriver())
        }
    }

    suspend operator fun <R> invoke(block: suspend (AppDatabase) -> R): R {
        initDatabase()
        return block(database!!)
    }
}