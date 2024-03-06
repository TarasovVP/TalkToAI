package com.vnstudio.talktoai.presentation.screens.settings.settings_language

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.vnstudio.talktoai.domain.models.ScreenState
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG_EN
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG_RU
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG_UK
import com.vnstudio.talktoai.presentation.components.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.painterRes
import com.vnstudio.talktoai.presentation.components.stringRes
import com.vnstudio.talktoai.presentation.theme.Neutral500
import com.vnstudio.talktoai.presentation.theme.Primary700
import com.vnstudio.talktoai.resources.DrawableResources
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsLanguageContent(
    screenState: ScreenState
) {

    val viewModel: SettingsLanguageViewModel = koinViewModel()
    val appLanguageState = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getAppLanguage()
    }
    val appLanguage = viewModel.appLanguageLiveData.collectAsState()
    LaunchedEffect(appLanguage.value) {
        appLanguageState.value = appLanguage.value
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .selectableGroup()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        SettingsLanguageItem(
            stringRes().SETTINGS_LANGUAGE_ENGLISH,
            APP_LANG_EN == appLanguageState.value,
            DrawableResources.IC_FLAG_EN
        ) {
            viewModel.setAppLanguage(APP_LANG_EN)
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(APP_LANG_EN))
        }
        SettingsLanguageItem(
            stringRes().SETTINGS_LANGUAGE_UKRAINIAN,
            APP_LANG_UK == appLanguageState.value,
            DrawableResources.IC_FLAG_UA
        ) {
            viewModel.setAppLanguage(APP_LANG_UK)
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(APP_LANG_UK))
        }
        SettingsLanguageItem(
            stringRes().SETTINGS_LANGUAGE_RUSSIAN,
            APP_LANG_RU == appLanguageState.value,
            DrawableResources.IC_FLAG_RU
        ) {
            viewModel.setAppLanguage(APP_LANG_RU)
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(APP_LANG_RU))
        }
    }
    ExceptionMessageHandler(screenState.infoMessageState, viewModel.exceptionLiveData)
}

@Composable
fun SettingsLanguageItem(name: String, isChecked: Boolean, icon: String, onLanguageCheck: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isChecked,
                onClick = onLanguageCheck,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Primary700,
                    unselectedColor = Neutral500
                )
            )
            Text(text = name, modifier = Modifier.weight(1f))
            Image(
                painter = painterRes(icon),
                contentDescription = "${stringRes().SETTINGS_LANGUAGE} $name"
            )
        }
    }
}