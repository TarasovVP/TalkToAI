package com.vnstudio.talktoai.presentation.screens.sealed_classes

import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen

sealed class SettingsScreen(val name: Int, val icon: Int, val route: String) {
    class ChatScreen : SettingsScreen(
        R.string.settings_chat,
        R.drawable.ic_settings_chat,
        NavigationScreen.SettingsChatScreen().route
    )

    class AccountScreen : SettingsScreen(
        R.string.settings_account,
        R.drawable.ic_settings_account,
        NavigationScreen.SettingsAccountScreen().route
    )

    class LanguageScreen : SettingsScreen(
        R.string.settings_language,
        R.drawable.ic_settings_language,
        NavigationScreen.SettingsLanguageScreen().route
    )

    class ThemeScreen : SettingsScreen(
        R.string.settings_theme,
        R.drawable.ic_settings_theme,
        NavigationScreen.SettingsThemeScreen().route
    )

    class FeedbackScreen : SettingsScreen(
        R.string.settings_feedback,
        R.drawable.ic_settings_feedback,
        NavigationScreen.SettingsFeedbackScreen().route
    )

    class PrivacyPolicyScreen : SettingsScreen(
        R.string.settings_privacy_policy,
        R.drawable.ic_settings_privacy,
        NavigationScreen.SettingsPrivacyPolicyScreen().route
    )

    companion object {
        val allSettingsScreens: List<SettingsScreen> = listOf(
            ChatScreen(),
            AccountScreen(),
            LanguageScreen(),
            ThemeScreen(),
            FeedbackScreen(),
            PrivacyPolicyScreen()
        )

        fun isSettingsScreen(route: String?) = allSettingsScreens.map { it.route }.contains(route)

        fun settingsScreenNameByRoute(route: String?) =
            allSettingsScreens.find { it.route == route }?.name ?: R.string.app_name
    }
}
