package presentation.screens.settings.settings_language

import androidx.compose.foundation.Image
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
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.ic_flag_en
import com.vnteam.talktoai.presentation.ui.components.ExceptionMessageHandler
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral500
import com.vnteam.talktoai.presentation.ui.theme.Primary700
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.SettingsLanguageViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

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
            LocalStringResources.current.SETTINGS_LANGUAGE_ENGLISH,
            Constants.APP_LANG_EN == appLanguageState.value,
            Res.drawable.ic_flag_en
        ) {
            viewModel.setAppLanguage(Constants.APP_LANG_EN)
        }
        SettingsLanguageItem(
            LocalStringResources.current.SETTINGS_LANGUAGE_UKRAINIAN,
            Constants.APP_LANG_UK == appLanguageState.value,
            Res.drawable.ic_flag_en
        ) {
            viewModel.setAppLanguage(Constants.APP_LANG_UK)
        }
    }
    ExceptionMessageHandler(screenState.infoMessageState, viewModel.exceptionLiveData)
}

@Composable
fun SettingsLanguageItem(
    name: String,
    isChecked: Boolean,
    icon: DrawableResource,
    onLanguageCheck: () -> Unit
) {
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
                painter = painterResource(icon),
                contentDescription = "${LocalStringResources.current.SETTINGS_LANGUAGE} $name"
            )
        }
    }
}