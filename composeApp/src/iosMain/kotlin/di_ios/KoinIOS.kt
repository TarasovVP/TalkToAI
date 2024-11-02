package di_ios

import com.vnteam.talktoai.di.appModule
import org.koin.core.context.startKoin

fun doInitKoin() = startKoin {
    modules(appModule, iosModule)
}