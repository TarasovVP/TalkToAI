package di_ios

import com.vnteam.talktoai.data.database.DatabaseDriverFactory
import com.vnteam.talktoai.data.local.PreferencesFactory
import org.koin.dsl.module

val iosModule = module {
    single {
        DatabaseDriverFactory()
    }
    single {
        PreferencesFactory()
    }
}