package com.vnteam.talktoai.data.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.vnteam.talktoai.AppDatabase
import com.vnteam.talktoai.data.DEMO_OBJECTS_DB

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseDriverFactory(private val context: android.content.Context) {
    actual suspend fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = AppDatabase.Schema.synchronous(),
            context,
            DEMO_OBJECTS_DB
        )
    }
}