package com.vnteam.talktoai.di_desktop

import com.vnteam.talktoai.data.JVM_APP_DIR_NAME
import com.vnteam.talktoai.data.JVM_USER_HOME_PROPERTY
import com.vnteam.talktoai.data.database.DatabaseDriverFactory
import com.vnteam.talktoai.data.filepicker.FilePicker
import com.vnteam.talktoai.data.filestorage.ImageCompressor
import com.vnteam.talktoai.data.filestorage.ImageStorage
import com.vnteam.talktoai.data.local.PreferencesFactory
import com.vnteam.talktoai.utils.AnimationUtils
import com.vnteam.talktoai.utils.NetworkState
import com.vnteam.talktoai.utils.ShareUtils
import org.koin.dsl.module
import java.io.File

val desktopModule = module {
    single { DatabaseDriverFactory() }
    single { PreferencesFactory() }
    single { NetworkState() }
    single { AnimationUtils() }
    single { ShareUtils() }
    single { FilePicker() }
    single {
        ImageStorage(File(System.getProperty(JVM_USER_HOME_PROPERTY), JVM_APP_DIR_NAME).resolve("images"))
    }
    single { ImageCompressor() }
}
