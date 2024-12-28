package presentation.screens.settings.settings_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.LocaleList
import com.vnteam.talktoai.CommonExtensions.flagDrawable
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.ic_settings
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Primary900
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.NavigationScreen.Companion.settingScreens
import com.vnteam.talktoai.presentation.ui.NavigationScreen.Companion.settingsScreenNameByRoute
import presentation.screens.chat_list.ChatItem

@Composable
fun SettingsListComposable(
    currentRouteState: String?,
    onNextScreen: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Primary900)
    ) {
        settingScreens.forEach { settingsScreen ->
            ChatItem(
                name = settingsScreenNameByRoute(
                    settingsScreen.route, LocalStringResources.current
                ),
                mainIcon = Res.drawable.ic_settings,
                isCurrent = currentRouteState == settingsScreen.route,
                secondaryIcon = if (settingsScreen.route == NavigationScreen.SettingsLanguageScreen().route) LocaleList.current.flagDrawable() else null
            ) {
                onNextScreen.invoke(settingsScreen.route)
            }
        }
    }
}