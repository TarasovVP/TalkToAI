package presentation.screens.settings.settings_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.CommonExtensions.flagDrawable
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.ic_settings_account
import com.vnteam.talktoai.ic_settings_chat
import com.vnteam.talktoai.ic_settings_feedback
import com.vnteam.talktoai.ic_settings_language
import com.vnteam.talktoai.ic_settings_privacy
import com.vnteam.talktoai.ic_settings_theme
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral50
import com.vnteam.talktoai.presentation.ui.theme.Primary800
import com.vnteam.talktoai.presentation.ui.theme.Primary900
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun SettingsListComposable(
    currentRouteState: String?,
    onNextScreen: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Primary900).padding(top = 8.dp)
    ) {
        SettingsItem(
            name = LocalStringResources.current.SETTINGS_CHAT,
            mainIcon = Res.drawable.ic_settings_chat,
            isCurrent = currentRouteState == NavigationScreen.SettingsChatScreen.route,
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsChatScreen.route)
        }
        SettingsItem(
            name = LocalStringResources.current.SETTINGS_ACCOUNT,
            mainIcon = Res.drawable.ic_settings_account,
            isCurrent = currentRouteState == NavigationScreen.SettingsAccountScreen.route,
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsAccountScreen.route)
        }
        SettingsItem(
            name = LocalStringResources.current.SETTINGS_LANGUAGE,
            mainIcon = Res.drawable.ic_settings_language,
            isCurrent = currentRouteState == NavigationScreen.SettingsLanguageScreen.route,
            secondaryIcon = LocaleList.current.flagDrawable()
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsLanguageScreen.route)
        }
        SettingsItem(
            name = LocalStringResources.current.SETTINGS_THEME,
            mainIcon = Res.drawable.ic_settings_theme,
            isCurrent = currentRouteState == NavigationScreen.SettingsThemeScreen.route,
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsThemeScreen.route)
        }
        SettingsItem(
            name = LocalStringResources.current.SETTINGS_FEEDBACK,
            mainIcon = Res.drawable.ic_settings_feedback,
            isCurrent = currentRouteState == NavigationScreen.SettingsFeedbackScreen.route,
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsFeedbackScreen.route)
        }
        SettingsItem(
            name = LocalStringResources.current.SETTINGS_PRIVACY_POLICY,
            mainIcon = Res.drawable.ic_settings_privacy,
            isCurrent = currentRouteState == NavigationScreen.SettingsPrivacyPolicyScreen.route,
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsPrivacyPolicyScreen.route)
        }
    }
}

@Composable
fun SettingsItem(
    name: String,
    mainIcon: DrawableResource,
    isCurrent: Boolean,
    secondaryIcon: DrawableResource? = null,
    onItemClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            .let { modifier ->
                if (isCurrent.not()) {
                    modifier.clickable {
                        onItemClick.invoke()
                    }
                } else {
                    modifier
                }
            }, colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) Primary800 else Primary900
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(mainIcon),
                contentDescription = name,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = name,
                color = Neutral50,
                modifier = Modifier.weight(1f).padding(vertical = 8.dp)
            )
            secondaryIcon?.let { icon ->
                Image(
                    painter = painterResource(icon),
                    contentDescription = name,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}