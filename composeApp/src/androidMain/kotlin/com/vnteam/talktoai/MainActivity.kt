package com.vnteam.talktoai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import com.vnteam.talktoai.data.filepicker.FilePicker
import com.vnteam.talktoai.presentation.App
import com.vnteam.talktoai.presentation.viewmodels.settings.AppViewModel
import com.vnteam.talktoai.utils.ShareUtils
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {

    private val filePicker: FilePicker by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filePicker.registerLauncher(this)
        setContent {
            val shareUtils = koinInject<ShareUtils>()
            SideEffect {
                shareUtils.setActivityProvider { this@MainActivity }
                filePicker.setActivityProvider { this@MainActivity }
            }
            App(koinViewModel<AppViewModel>())
        }
    }
}
