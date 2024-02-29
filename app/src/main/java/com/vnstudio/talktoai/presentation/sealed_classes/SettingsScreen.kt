package com.vnstudio.talktoai.presentation.sealed_classes

import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.resources.StringResources

sealed class SettingsScreen(val name: String, val icon: String, val route: String) {
    class ChatScreen : SettingsScreen(
        "settings_chat",
        "ic_settings_chat",
        NavigationScreen.SettingsChatScreen().route
    )

    class AccountScreen : SettingsScreen(
        "settings_account",
        "ic_settings_account",
        NavigationScreen.SettingsAccountScreen().route
    )

    class LanguageScreen : SettingsScreen(
        "settings_language",
        "ic_settings_language",
        NavigationScreen.SettingsLanguageScreen().route
    )

    class ThemeScreen : SettingsScreen(
        "settings_theme",
        "ic_settings_theme",
        NavigationScreen.SettingsThemeScreen().route
    )

    class FeedbackScreen : SettingsScreen(
        "settings_feedback",
        "ic_settings_feedback",
        NavigationScreen.SettingsFeedbackScreen().route
    )

    class PrivacyPolicyScreen : SettingsScreen(
        "settings_privacy_policy",
        "ic_settings_privacy",
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

        fun settingsScreenNameByRoute(route: String?, stringRes: StringResources): String {
            return when(route) {
                NavigationScreen.SettingsChatScreen().route -> return stringRes.SETTINGS_CHAT
                NavigationScreen.SettingsAccountScreen().route -> return stringRes.SETTINGS_ACCOUNT
                NavigationScreen.SettingsLanguageScreen().route -> return stringRes.SETTINGS_LANGUAGE
                NavigationScreen.SettingsThemeScreen().route -> return stringRes.SETTINGS_THEME
                NavigationScreen.SettingsFeedbackScreen().route -> return stringRes.SETTINGS_FEEDBACK
                NavigationScreen.SettingsPrivacyPolicyScreen().route -> return stringRes.SETTINGS_PRIVACY_POLICY
                else -> stringRes.APP_NAME
            }
        }
    }
}
