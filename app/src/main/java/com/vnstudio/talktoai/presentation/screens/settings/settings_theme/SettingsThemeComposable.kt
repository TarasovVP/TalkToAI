package com.vnstudio.talktoai.presentation.screens.settings.settings_theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.presentation.components.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.theme.Neutral500
import com.vnstudio.talktoai.presentation.theme.Primary700

@Composable
fun SettingsThemeScreen(infoMessageState: MutableState<InfoMessage?>) {

    val viewModel: SettingsThemeViewModel = koinViewModel()
    val appThemeState = remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getAppTheme()
    }
    val appTheme = viewModel.appThemeLiveData.observeAsState()
    LaunchedEffect(appTheme.value) {
        appThemeState.value = appTheme.value
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .selectableGroup()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        SettingsThemeItem(
            stringResource(id = R.string.settings_theme_day),
            AppCompatDelegate.MODE_NIGHT_NO == appThemeState.value
        ) {
            viewModel.setAppTheme(AppCompatDelegate.MODE_NIGHT_NO)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        SettingsThemeItem(
            stringResource(id = R.string.settings_theme_night),
            AppCompatDelegate.MODE_NIGHT_YES == appThemeState.value
        ) {
            viewModel.setAppTheme(AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        SettingsThemeItem(
            stringResource(id = R.string.settings_theme_auto),
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM == appThemeState.value
        ) {
            viewModel.setAppTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
}

@Composable
fun SettingsThemeItem(name: String, isChecked: Boolean, onThemeModeCheck: () -> Unit) {

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
                onClick = onThemeModeCheck,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Primary700,
                    unselectedColor = Neutral500
                )
            )
            Text(text = name)
        }
    }
}