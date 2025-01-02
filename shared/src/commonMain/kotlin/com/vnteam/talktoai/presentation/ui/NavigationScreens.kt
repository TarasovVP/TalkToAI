package com.vnteam.talktoai.presentation.ui

import com.vnteam.talktoai.presentation.ui.resources.StringResources

sealed class NavigationScreen(val route: String) {
    data object OnboardingScreen : NavigationScreen(ONBOARDING_SCREEN)

    data object LoginScreen : NavigationScreen(LOGIN_SCREEN)

    data object SignUpScreen : NavigationScreen(SIGN_UP_SCREEN)

    data object ChatScreen : NavigationScreen(CHAT_SCREEN)

    data object SettingsChatScreen : NavigationScreen(SETTINGS_CHAT_SCREEN)

    data object SettingsAccountScreen : NavigationScreen(SETTINGS_ACCOUNT_SCREEN)

    data object SettingsSignUpScreen : NavigationScreen(SETTINGS_SIGN_UP_SCREEN)

    data object SettingsLanguageScreen : NavigationScreen(SETTINGS_LANGUAGE_SCREEN)

    data object SettingsThemeScreen : NavigationScreen(SETTINGS_THEME_SCREEN)

    data object SettingsFeedbackScreen : NavigationScreen(SETTINGS_FEEDBACK_SCREEN)

    data object SettingsPrivacyPolicyScreen : NavigationScreen(SETTINGS_PRIVACY_POLICY_SCREEN)

    companion object {

        const val ONBOARDING_SCREEN = "destination_onboarding_screen"
        const val LOGIN_SCREEN = "destination_login_screen"
        const val SIGN_UP_SCREEN = "destination_sign_up_screen"
        const val CHAT_DESTINATION = "destination_chat_screen"
        const val CHAT_SCREEN = "$CHAT_DESTINATION/{chatId}"
        const val SETTINGS_CHAT_SCREEN = "destination_settings_chat_screen"
        const val SETTINGS_ACCOUNT_SCREEN = "destination_settings_account_screen"
        const val SETTINGS_LANGUAGE_SCREEN = "destination_settings_language_screen"
        const val SETTINGS_THEME_SCREEN = "destination_settings_theme_screen"
        const val SETTINGS_FEEDBACK_SCREEN = "destination_settings_feedback_screen"
        const val SETTINGS_PRIVACY_POLICY_SCREEN = "destination_settings_privacy_policy_screen"
        const val SETTINGS_SIGN_UP_SCREEN = "destination_settings_sign_up_screen"

        val settingScreens = listOf(
            SettingsChatScreen,
            SettingsAccountScreen,
            SettingsLanguageScreen,
            SettingsThemeScreen,
            SettingsFeedbackScreen,
            SettingsPrivacyPolicyScreen
        )

        fun isSettingsScreen(route: String?): Boolean {
            return route in settingScreens.map { it.route }
        }

        fun settingsScreenNameByRoute(route: String?, stringRes: StringResources): String {
            return when (route) {
                SettingsChatScreen.route -> return stringRes.SETTINGS_CHAT
                SettingsAccountScreen.route -> return stringRes.SETTINGS_ACCOUNT
                SettingsLanguageScreen.route -> return stringRes.SETTINGS_LANGUAGE
                SettingsThemeScreen.route -> return stringRes.SETTINGS_THEME
                SettingsFeedbackScreen.route -> return stringRes.SETTINGS_FEEDBACK
                SettingsPrivacyPolicyScreen.route -> return stringRes.SETTINGS_PRIVACY_POLICY
                else -> stringRes.APP_NAME
            }
        }
    }
}
