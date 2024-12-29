package presentation.screens.settings.settings_theme

import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral500
import com.vnteam.talktoai.presentation.ui.theme.Primary700
import com.vnteam.talktoai.presentation.viewmodels.SettingsThemeViewModel
import org.koin.compose.viewmodel.koinViewModel
import presentation.updateScreenState

@Composable
fun SettingsThemeContent() {

    val viewModel: SettingsThemeViewModel = koinViewModel()
    updateScreenState(viewModel.progressVisibilityState.collectAsState().value)

    LaunchedEffect(Unit) {
        viewModel.getIsDarkTheme()
    }
    val appTheme = viewModel.isDarkTheme.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .selectableGroup()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        SettingsThemeItem(
            LocalStringResources.current.SETTINGS_THEME_DAY,
            isSystemInDarkTheme() == appTheme.value
        ) {
            viewModel.setIsDarkTheme(false)
        }
        SettingsThemeItem(
            LocalStringResources.current.SETTINGS_THEME_NIGHT,
            isSystemInDarkTheme() != appTheme.value
        ) {
            viewModel.setIsDarkTheme(true)
        }
    }
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