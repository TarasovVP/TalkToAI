package com.vnstudio.talktoai.presentation.screens.settings.settings_language

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG_EN
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG_RU
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG_UK
import com.vnstudio.talktoai.presentation.screens.base.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.theme.Neutral500
import com.vnstudio.talktoai.presentation.theme.Primary700

@Composable
fun SettingsLanguageScreen(infoMessageState: MutableState<InfoMessage?>) {

    val viewModel: SettingsLanguageViewModel = hiltViewModel()
    val appLanguageState = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getAppLanguage()
    }
    val appLanguage = viewModel.appLanguageLiveData.observeAsState()
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
        SettingsLanguageItem(stringResource(id = R.string.settings_language_english), APP_LANG_EN == appLanguageState.value, R.drawable.ic_flag_en) {
            viewModel.setAppLanguage(APP_LANG_EN)
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(APP_LANG_EN))
        }
        SettingsLanguageItem(stringResource(id = R.string.settings_language_ukrainian), APP_LANG_UK == appLanguageState.value, R.drawable.ic_flag_ua) {
            viewModel.setAppLanguage(APP_LANG_UK)
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(APP_LANG_UK))
        }
        SettingsLanguageItem(stringResource(id = R.string.settings_language_russian), APP_LANG_RU == appLanguageState.value, R.drawable.ic_flag_ru) {
            viewModel.setAppLanguage(APP_LANG_RU)
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(APP_LANG_RU))
        }
    }
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
}

@Composable
fun SettingsLanguageItem(name: String, isChecked: Boolean, icon: Int, onLanguageCheck: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = isChecked, onClick = onLanguageCheck, colors = RadioButtonDefaults.colors(
                selectedColor = Primary700,
                unselectedColor = Neutral500
            ))
            Text(text = name, modifier = Modifier.weight(1f))
            Image(imageVector = ImageVector.vectorResource(id = icon), contentDescription = "App language $name")
        }
    }
}