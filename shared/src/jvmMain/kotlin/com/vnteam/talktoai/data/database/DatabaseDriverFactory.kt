package com.vnteam.talktoai.data.database

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.async.coroutines.awaitMigrate
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.vnteam.talktoai.AppDatabase
import com.vnteam.talktoai.data.DEMO_OBJECTS_DB
import com.vnteam.talktoai.data.JVM_APP_DIR_NAME
import com.vnteam.talktoai.data.JVM_USER_HOME_PROPERTY
import java.io.File

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        val dbDir = File(System.getProperty(JVM_USER_HOME_PROPERTY), JVM_APP_DIR_NAME).also { it.mkdirs() }
        val dbFile = File(dbDir, DEMO_OBJECTS_DB)
        val isNewDb = !dbFile.exists()
        val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
        val schemaVersion = AppDatabase.Schema.version
        when {
            isNewDb -> {
                AppDatabase.Schema.awaitCreate(driver)
                driver.execute(null, "PRAGMA user_version = $schemaVersion", 0)
            }
            else -> {
                val storedVersion = driver.executeQuery(
                    identifier = null,
                    sql = "PRAGMA user_version",
                    mapper = { cursor -> QueryResult.Value(if (cursor.next().value) cursor.getLong(0) ?: 0L else 0L) },
                    parameters = 0,
                ).value
                val currentVersion = if (storedVersion == 0L) detectLegacyVersion(driver) else storedVersion
                if (currentVersion != storedVersion) {
                    driver.execute(null, "PRAGMA user_version = $currentVersion", 0)
                }
                if (currentVersion < schemaVersion) {
                    AppDatabase.Schema.awaitMigrate(driver, currentVersion, schemaVersion)
                    driver.execute(null, "PRAGMA user_version = $schemaVersion", 0)
                }
            }
        }
        return driver
    }

    private fun detectLegacyVersion(driver: SqlDriver): Long {
        return try {
            val columns = driver.executeQuery(
                identifier = null,
                sql = "PRAGMA table_info(ChatDB)",
                mapper = { cursor ->
                    val names = mutableSetOf<String>()
                    while (cursor.next().value) { cursor.getString(1)?.let { names.add(it) } }
                    QueryResult.Value(names)
                },
                parameters = 0,
            ).value
            when {
                "aiProvider" in columns -> 4L
                "context" in columns -> 3L
                "aiModel" in columns -> 2L
                columns.isNotEmpty() -> 1L
                else -> 0L
            }
        } catch (e: Exception) { 0L }
    }
}
