package presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import org.koin.core.component.KoinComponent
import presentation.screens.settings.settings_account.SettingsAccountContent
import presentation.screens.settings.settings_chat.SettingsChatContent
import presentation.screens.settings.settings_feedback.SettingsFeedbackContent
import presentation.screens.settings.settings_language.SettingsLanguageContent
import presentation.screens.settings.settings_privacy_policy.SettingsPrivacyPolicyContent
import presentation.screens.settings.settings_sign_up.SettingsSignUpContent
import presentation.screens.settings.settings_theme.SettingsThemeContent

sealed class NavigationScreen(val route: String, val icon: String = String.EMPTY): Screen,
    KoinComponent {
    class OnboardingScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(ONBOARDING_SCREEN) {
        @Composable
        override fun Content() {
            //OnboardingContent(screenState)
        }
    }
    class LoginScreen(private val screenState: ScreenState = ScreenState()) : NavigationScreen( LOGIN_SCREEN) {
        @Composable
        override fun Content() {
            //LoginContent(screenState)
        }
    }
    class SignUpScreen(private val screenState: ScreenState = ScreenState()) : NavigationScreen(
        SIGN_UP_SCREEN
    ) {
        @Composable
        override fun Content() {
            //SignUpContent(screenState)
        }
    }
    class ChatScreen(
        private val chatId: Long = -1L,
        private val showCreateChatDialogue: MutableState<Boolean> = mutableStateOf(false),
        private val isMessageActionModeState: MutableState<Boolean?> = mutableStateOf(false),
        private val screenState: ScreenState = ScreenState()
    ) : NavigationScreen(CHAT_SCREEN, DrawableResources.IC_CHAT) {
        @Composable
        override fun Content() {
            //ChatContent(chatId, showCreateChatDialogue, isMessageActionModeState, screenState)
        }
    }
    class SettingsChatScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_CHAT_SCREEN, DrawableResources.IC_SETTINGS_CHAT) {
        @Composable
        override fun Content() {
            SettingsChatContent(screenState)
        }
    }
    class SettingsAccountScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_ACCOUNT_SCREEN, DrawableResources.IC_SETTINGS_ACCOUNT) {
        @Composable
        override fun Content() {
            SettingsAccountContent(screenState)
        }
    }
    class SettingsSignUpScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_SIGN_UP_SCREEN) {
        @Composable
        override fun Content() {
            SettingsSignUpContent(screenState)
        }
    }
    class SettingsLanguageScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_LANGUAGE_SCREEN, DrawableResources.IC_SETTINGS_LANGUAGE) {
        @Composable
        override fun Content() {
            SettingsLanguageContent(screenState)
        }
    }
    class SettingsThemeScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_THEME_SCREEN, DrawableResources.IC_SETTINGS_THEME) {
        @Composable
        override fun Content() {
            SettingsThemeContent(screenState)
        }
    }
    class SettingsFeedbackScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen(SETTINGS_FEEDBACK_SCREEN, DrawableResources.IC_SETTINGS_FEEDBACK) {
        @Composable
        override fun Content() {
            SettingsFeedbackContent(screenState)
        }
    }
    class SettingsPrivacyPolicyScreen(private val screenState: ScreenState = ScreenState()) :
        NavigationScreen( SETTINGS_PRIVACY_POLICY_SCREEN, DrawableResources.IC_SETTINGS_PRIVACY) {
        @Composable
        override fun Content() {
            SettingsPrivacyPolicyContent(screenState)
        }
    }

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
            return when(route) {
                SettingsChatScreen().route -> return StringResources.SETTINGS_CHAT
                SettingsAccountScreen().route -> return StringResources.SETTINGS_ACCOUNT
                SettingsLanguageScreen().route -> return StringResources.SETTINGS_LANGUAGE
                SettingsThemeScreen().route -> return StringResources.SETTINGS_THEME
                SettingsFeedbackScreen().route -> return StringResources.SETTINGS_FEEDBACK
                SettingsPrivacyPolicyScreen().route -> return StringResources.SETTINGS_PRIVACY_POLICY
                else -> StringResources.APP_NAME
            }
        }

        fun isChatScreen(route: String?) = route == ChatScreen().route

        fun fromRoute(screenState: ScreenState): Screen {
            return when(screenState.currentScreenState.value) {
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

object StringResources {
    const val SETTINGS_CHAT = "settings_chat"
    const val SETTINGS_ACCOUNT = "settings_account"
    const val SETTINGS_LANGUAGE = "settings_language"
    const val SETTINGS_THEME = "settings_theme"
    const val SETTINGS_FEEDBACK = "settings_feedback"
    const val SETTINGS_PRIVACY_POLICY = "settings_privacy_policy"
    const val APP_NAME = "app_name"
}

interface Screen {
    @Composable
    fun Content()
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
