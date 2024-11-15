package presentation.screens.settings.settings_theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.SettingsThemeViewModel
import components.ExceptionMessageHandler
import org.koin.compose.viewmodel.koinViewModel
import theme.Neutral500
import theme.Primary700

@Composable
fun SettingsThemeContent(screenState: ScreenState) {

    val viewModel: SettingsThemeViewModel = koinViewModel()
    val appThemeState = remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getAppTheme()
    }
    val appTheme = viewModel.appThemeLiveData.collectAsState()
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
        /*SettingsThemeItem(
            LocalStringResources.current.SETTINGS_THEME_DAY,
            AppCompatDelegate.MODE_NIGHT_NO == appThemeState.value
        ) {
            viewModel.setAppTheme(AppCompatDelegate.MODE_NIGHT_NO)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        SettingsThemeItem(
            LocalStringResources.current.SETTINGS_THEME_NIGHT,
            AppCompatDelegate.MODE_NIGHT_YES == appThemeState.value
        ) {
            viewModel.setAppTheme(AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        SettingsThemeItem(
            LocalStringResources.current.SETTINGS_THEME_AUTO,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM == appThemeState.value
        ) {
            viewModel.setAppTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }*/
    }
    ExceptionMessageHandler(screenState.infoMessageState, viewModel.exceptionLiveData)
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