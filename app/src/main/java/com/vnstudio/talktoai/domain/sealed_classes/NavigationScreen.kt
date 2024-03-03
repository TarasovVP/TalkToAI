package com.vnstudio.talktoai.domain.sealed_classes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.screen.Screen
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.domain.models.ScreenState
import com.vnstudio.talktoai.presentation.screens.authorization.login.LoginContent
import com.vnstudio.talktoai.presentation.screens.authorization.onboarding.OnboardingContent
import com.vnstudio.talktoai.presentation.screens.authorization.signup.SignUpContent
import com.vnstudio.talktoai.presentation.screens.chat.ChatContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_account.SettingsAccountContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_chat.SettingsChatContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_feedback.SettingsFeedbackContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_language.SettingsLanguageContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_privacy_policy.SettingsPrivacyPolicyContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_sign_up.SettingsSignUpContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_theme.SettingsThemeContent
import com.vnstudio.talktoai.resources.StringResources
import org.koin.core.component.KoinComponent

sealed class NavigationScreen(val route: String, val icon: String = String.EMPTY): Screen, KoinComponent {
    class OnboardingScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen("destination_onboarding_screen", "ic_onboarding") {
        @Composable
        override fun Content() {
            OnboardingContent(screenState)
        }
    }
    class LoginScreen(private val screenState: ScreenState = ScreenState()) : NavigationScreen( "destination_login_screen", "ic_login") {
        @Composable
        override fun Content() {
            LoginContent(screenState)
        }
    }
    class SignUpScreen(private val screenState: ScreenState = ScreenState()) : NavigationScreen("destination_sign_up_screen", "ic_sign_up") {
        @Composable
        override fun Content() {
            SignUpContent(screenState)
        }
    }
    class ChatScreen(private val screenState: ScreenState = ScreenState()) : NavigationScreen("destination_chat_screen/{current_chat_id}", "ic_chat") {
        @Composable
        override fun Content() {
            ChatContent(screenState = screenState)
        }
    }
    class SettingsChatScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen("destination_settings_chat_screen", "ic_settings_chat") {
        @Composable
        override fun Content() {
            SettingsChatContent(screenState)
        }
    }
    class SettingsAccountScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen("destination_settings_account_screen", "ic_settings_account") {
        @Composable
        override fun Content() {
            SettingsAccountContent(screenState)
        }
    }
    class SettingsSignUpScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen("destination_settings_sign_up_screen", "ic_settings_sign_up") {
        @Composable
        override fun Content() {
            SettingsSignUpContent(screenState)
        }
    }
    class SettingsLanguageScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen("destination_settings_language_screen", "ic_settings_language") {
        @Composable
        override fun Content() {
            SettingsLanguageContent(screenState)
        }
    }
    class SettingsThemeScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen("destination_settings_theme_screen", "ic_settings_theme") {
        @Composable
        override fun Content() {
            SettingsThemeContent(screenState)
        }
    }
    class SettingsFeedbackScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen("destination_settings_feedback_screen", "ic_settings_feedback") {
        @Composable
        override fun Content() {
            SettingsFeedbackContent(screenState)
        }
    }
    class SettingsPrivacyPolicyScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen( "destination_settings_privacy_policy_screen", "ic_settings_privacy") {
        @Composable
        override fun Content() {
            SettingsPrivacyPolicyContent(screenState)
        }
    }

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

        fun fromRoute(screenState: ScreenState): Screen {
            return when(screenState.nextScreenState.value) {
                OnboardingScreen().route -> OnboardingScreen(screenState)
                LoginScreen().route -> LoginScreen(screenState)
                SignUpScreen().route -> SignUpScreen(screenState)
                SettingsChatScreen().route -> SettingsChatScreen(screenState)
                SettingsAccountScreen().route -> SettingsAccountScreen(screenState)
                SettingsLanguageScreen().route -> SettingsLanguageScreen(screenState)
                SettingsThemeScreen().route -> SettingsThemeScreen(screenState)
                SettingsFeedbackScreen().route -> SettingsFeedbackScreen(screenState)
                SettingsPrivacyPolicyScreen().route -> SettingsPrivacyPolicyScreen(screenState)
                else -> ChatScreen(screenState)
            }
        }
    }
}
