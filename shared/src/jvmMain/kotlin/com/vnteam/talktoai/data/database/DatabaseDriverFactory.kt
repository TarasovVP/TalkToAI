package com.vnteam.talktoai.data.database

import app.cash.sqldelight.async.coroutines.awaitCreate
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
        if (isNewDb) {
            AppDatabase.Schema.awaitCreate(driver)
        } else {
            applyMissingColumns(driver)
        }
        return driver
    }

    private fun applyMissingColumns(driver: SqlDriver) {
        listOf(
            "ALTER TABLE ChatDB ADD COLUMN aiModel TEXT",
            "ALTER TABLE ChatDB ADD COLUMN temperature REAL",
            "ALTER TABLE ChatDB ADD COLUMN context TEXT",
            "ALTER TABLE ChatDB ADD COLUMN aiProvider TEXT",
            "ALTER TABLE MessageDB ADD COLUMN contentJson TEXT",
        ).forEach { sql ->
            try { driver.execute(null, sql, 0) } catch (_: Exception) { }
        }
    }
}
