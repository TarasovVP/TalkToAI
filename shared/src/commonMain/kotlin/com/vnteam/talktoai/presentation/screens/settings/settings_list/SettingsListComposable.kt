package com.vnteam.talktoai.presentation.screens.settings.settings_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.ic_arrow_forward
import com.vnteam.talktoai.ic_flag_en
import com.vnteam.talktoai.ic_flag_ua
import com.vnteam.talktoai.ic_settings_account
import com.vnteam.talktoai.ic_settings_chat
import com.vnteam.talktoai.ic_settings_feedback
import com.vnteam.talktoai.ic_settings_language
import com.vnteam.talktoai.ic_settings_privacy
import com.vnteam.talktoai.ic_settings_theme
import com.vnteam.talktoai.presentation.LocalScreenState
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral100
import com.vnteam.talktoai.presentation.ui.theme.Neutral200
import com.vnteam.talktoai.presentation.ui.theme.Neutral500
import com.vnteam.talktoai.presentation.ui.theme.Neutral800
import com.vnteam.talktoai.presentation.ui.theme.Primary600
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun SettingsListComposable(
    currentRouteState: String?,
    onNextScreen: (String) -> Unit,
) {
    val language = LocalScreenState.current.value.language
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Neutral100)
            .padding(top = 16.dp)
    ) {
        SettingsItem(
            name = LocalStringResources.current.SETTINGS_CHAT,
            mainIcon = Res.drawable.ic_settings_chat,
            isCurrent = currentRouteState == NavigationScreen.SettingsChatScreen.route,
        ) { onNextScreen(NavigationScreen.SettingsChatScreen.route) }
        SettingsItem(
            name = LocalStringResources.current.SETTINGS_ACCOUNT,
            mainIcon = Res.drawable.ic_settings_account,
            isCurrent = currentRouteState == NavigationScreen.SettingsAccountScreen.route,
        ) { onNextScreen(NavigationScreen.SettingsAccountScreen.route) }
        SettingsItem(
            name = LocalStringResources.current.SETTINGS_LANGUAGE,
            mainIcon = Res.drawable.ic_settings_language,
            isCurrent = currentRouteState == NavigationScreen.SettingsLanguageScreen.route,
            secondaryIcon = if (language == Constants.APP_LANG_UK) Res.drawable.ic_flag_ua else Res.drawable.ic_flag_en,
        ) { onNextScreen(NavigationScreen.SettingsLanguageScreen.route) }
        SettingsItem(
            name = LocalStringResources.current.SETTINGS_THEME,
            mainIcon = Res.drawable.ic_settings_theme,
            isCurrent = currentRouteState == NavigationScreen.SettingsThemeScreen.route,
        ) { onNextScreen(NavigationScreen.SettingsThemeScreen.route) }
        SettingsItem(
            name = LocalStringResources.current.SETTINGS_FEEDBACK,
            mainIcon = Res.drawable.ic_settings_feedback,
            isCurrent = currentRouteState == NavigationScreen.SettingsFeedbackScreen.route,
        ) { onNextScreen(NavigationScreen.SettingsFeedbackScreen.route) }
        SettingsItem(
            name = LocalStringResources.current.SETTINGS_PRIVACY_POLICY,
            mainIcon = Res.drawable.ic_settings_privacy,
            isCurrent = currentRouteState == NavigationScreen.SettingsPrivacyPolicyScreen.route,
        ) { onNextScreen(NavigationScreen.SettingsPrivacyPolicyScreen.route) }
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
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isCurrent) Neutral200 else Neutral100)
                .clickable(enabled = !isCurrent, onClick = onItemClick)
                .padding(horizontal = 24.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(mainIcon),
                contentDescription = name,
                tint = Primary600,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = name,
                color = Neutral800,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
            )
            secondaryIcon?.let {
                Icon(
                    painter = painterResource(it),
                    contentDescription = null,
                    tint = androidx.compose.ui.graphics.Color.Unspecified,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_forward),
                contentDescription = null,
                tint = Neutral500,
                modifier = Modifier.size(20.dp),
            )
        }
        HorizontalDivider(color = Neutral200, thickness = 1.dp)
    }
}
