package com.vnteam.talktoai.presentation.ui

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.presentation.ui.resources.StringResources
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState

sealed class NavigationScreen(
    val route: String,
    val name: String = String.EMPTY,
    val icon: String = String.EMPTY
) {
    class OnboardingScreen(private var screenState: ScreenState = ScreenState()) : NavigationScreen(ONBOARDING_SCREEN)

    class LoginScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(LOGIN_SCREEN)

    class SignUpScreen(private val screenState: ScreenState = ScreenState()) : NavigationScreen(
        SIGN_UP_SCREEN
    )

    class ChatScreen(
        private val screenState: ScreenState? = ScreenState()
    ) : NavigationScreen(CHAT_SCREEN, DrawableResources.IC_CHAT)

    class SettingsChatScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_CHAT_SCREEN, DrawableResources.IC_SETTINGS_CHAT)

    class SettingsAccountScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_ACCOUNT_SCREEN, DrawableResources.IC_SETTINGS_ACCOUNT)

    class SettingsSignUpScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_SIGN_UP_SCREEN)

    class SettingsLanguageScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_LANGUAGE_SCREEN, DrawableResources.IC_SETTINGS_LANGUAGE)

    class SettingsThemeScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_THEME_SCREEN, DrawableResources.IC_SETTINGS_THEME)

    class SettingsFeedbackScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_FEEDBACK_SCREEN, DrawableResources.IC_SETTINGS_FEEDBACK)

    class SettingsPrivacyPolicyScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_PRIVACY_POLICY_SCREEN, DrawableResources.IC_SETTINGS_PRIVACY)

    companion object {

        const val ONBOARDING_SCREEN = "destination_onboarding_screen"
        const val LOGIN_SCREEN = "destination_login_screen"
        const val SIGN_UP_SCREEN = "destination_sign_up_screen"
        const val CHAT_SCREEN = "destination_chat_screen/{current_chat_id}"
        const val SETTINGS_CHAT_SCREEN = "destination_settings_chat_screen"
        const val SETTINGS_ACCOUNT_SCREEN = "destination_settings_account_screen"
        const val SETTINGS_LANGUAGE_SCREEN = "destination_settings_language_screen"
        const val SETTINGS_THEME_SCREEN = "destination_settings_theme_screen"
        const val SETTINGS_FEEDBACK_SCREEN = "destination_settings_feedback_screen"
        const val SETTINGS_PRIVACY_POLICY_SCREEN = "destination_settings_privacy_policy_screen"
        const val SETTINGS_SIGN_UP_SCREEN = "destination_settings_sign_up_screen"

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
            return when (route) {
                SettingsChatScreen().route -> return stringRes.SETTINGS_CHAT
                SettingsAccountScreen().route -> return stringRes.SETTINGS_ACCOUNT
                SettingsLanguageScreen().route -> return stringRes.SETTINGS_LANGUAGE
                SettingsThemeScreen().route -> return stringRes.SETTINGS_THEME
                SettingsFeedbackScreen().route -> return stringRes.SETTINGS_FEEDBACK
                SettingsPrivacyPolicyScreen().route -> return stringRes.SETTINGS_PRIVACY_POLICY
                else -> stringRes.APP_NAME
            }
        }

        fun isChatScreen(route: String?) = route == ChatScreen().route

        fun fromRoute(screenState: ScreenState?): NavigationScreen {
            return when (screenState?.currentScreenRoute) {
                OnboardingScreen().route -> OnboardingScreen(screenState)
                LoginScreen().route -> LoginScreen(screenState)
                SignUpScreen().route -> SignUpScreen(screenState)
                SettingsChatScreen().route -> SettingsChatScreen(screenState)
                SettingsAccountScreen().route -> SettingsAccountScreen(screenState)
                SettingsLanguageScreen().route -> SettingsLanguageScreen(screenState)
                SettingsThemeScreen().route -> SettingsThemeScreen(screenState)
                SettingsFeedbackScreen().route -> SettingsFeedbackScreen(screenState)
                SettingsPrivacyPolicyScreen().route -> SettingsPrivacyPolicyScreen(screenState)
                else -> ChatScreen(screenState = screenState)
            }
        }
    }
}

object DrawableResources {
    const val IC_CHAT = "ic_chat"
    const val IC_SETTINGS_CHAT = "ic_settings_chat"
    const val IC_SETTINGS_ACCOUNT = "ic_settings_account"
    const val IC_SETTINGS_LANGUAGE = "ic_settings_language"
    const val IC_SETTINGS_THEME = "ic_settings_theme"
    const val IC_SETTINGS_FEEDBACK = "ic_settings_feedback"
    const val IC_SETTINGS_PRIVACY = "ic_settings_privacy"
}
