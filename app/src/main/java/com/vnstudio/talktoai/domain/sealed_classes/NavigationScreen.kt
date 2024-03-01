package com.vnstudio.talktoai.domain.sealed_classes

import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.resources.StringResources

sealed class NavigationScreen(val route: String, val icon: String = String.EMPTY) {
    class OnboardingScreen :
        NavigationScreen("destination_onboarding_screen", "ic_onboarding")
    class LoginScreen : NavigationScreen( "destination_login_screen", "ic_login")
    class SignUpScreen : NavigationScreen("destination_sign_up_screen", "ic_sign_up")
    class ChatScreen : NavigationScreen("destination_chat_screen/{current_chat_id}", "ic_chat")
    class SettingsChatScreen :
        NavigationScreen("destination_settings_chat_screen", "ic_settings_chat")
    class SettingsAccountScreen :
        NavigationScreen("destination_settings_account_screen", "ic_settings_account")
    class SettingsSignUpScreen :
        NavigationScreen("destination_settings_sign_up_screen", "ic_settings_sign_up")
    class SettingsLanguageScreen :
        NavigationScreen("destination_settings_language_screen", "ic_settings_language")
    class SettingsThemeScreen :
        NavigationScreen("destination_settings_theme_screen", "ic_settings_theme")
    class SettingsFeedbackScreen :
        NavigationScreen("destination_settings_feedback_screen", "ic_settings_feedback")
    class SettingsPrivacyPolicyScreen :
        NavigationScreen( "destination_settings_privacy_policy_screen", "ic_settings_privacy")

    companion object {

        val settingScreens = listOf(
            SettingsChatScreen(),
            SettingsAccountScreen(),
            SettingsLanguageScreen(),
            SettingsThemeScreen(),
            SettingsFeedbackScreen(),
            SettingsPrivacyPolicyScreen()
        )
        fun isSettingsScreen(route: String?): Boolean {
            return route in settingScreens.map { it.route }
        }
        fun settingsScreenNameByRoute(route: String?, stringRes: StringResources): String {
            return when(route) {
                SettingsChatScreen().route -> return stringRes.SETTINGS_CHAT
                SettingsAccountScreen().route -> return stringRes.SETTINGS_ACCOUNT
                SettingsLanguageScreen().route -> return stringRes.SETTINGS_LANGUAGE
                SettingsThemeScreen().route -> return stringRes.SETTINGS_THEME
                SettingsFeedbackScreen().route -> return stringRes.SETTINGS_FEEDBACK
                SettingsPrivacyPolicyScreen().route -> return stringRes.SETTINGS_PRIVACY_POLICY
                else -> stringRes.APP_NAME
            }
        }
    }
}
