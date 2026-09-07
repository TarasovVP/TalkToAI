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
        if (isNewDb) {
            AppDatabase.Schema.awaitCreate(driver)
            driver.execute(null, "PRAGMA user_version = $schemaVersion", 0)
        } else {
            val actualVersion = detectActualVersion(driver)
            driver.execute(null, "PRAGMA user_version = $actualVersion", 0)
            if (actualVersion < schemaVersion) {
                AppDatabase.Schema.awaitMigrate(driver, actualVersion, schemaVersion)
                driver.execute(null, "PRAGMA user_version = $schemaVersion", 0)
            }
        }
        return driver
    }

    private fun detectActualVersion(driver: SqlDriver): Long {
        fun hasColumn(column: String): Boolean = try {
            driver.executeQuery(
                identifier = null,
                sql = "SELECT $column FROM ChatDB LIMIT 0",
                mapper = { QueryResult.Value(true) },
                parameters = 0,
            ).value
        } catch (e: Exception) { false }

        return when {
            hasColumn("aiProvider") -> 4L
            hasColumn("context") -> 3L
            hasColumn("aiModel") -> 2L
            hasColumn("id") -> 1L
            else -> 0L
        }
    }
}
