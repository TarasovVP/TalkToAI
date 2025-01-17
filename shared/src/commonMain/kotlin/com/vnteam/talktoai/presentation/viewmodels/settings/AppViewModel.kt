package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.data.APP_LANG_EN
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.LanguageUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ThemeUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserLoginUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import com.vnteam.talktoai.utils.AnimationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import org.jetbrains.compose.resources.ExperimentalResourceApi

class AppViewModel(
    private val themeUseCase: ThemeUseCase,
    private val languageUseCase: LanguageUseCase,
    private val userLoginUseCase: UserLoginUseCase,
    private val onboardingUseCase: OnboardingUseCase,
    val animationUtils: AnimationUtils
) : BaseViewModel() {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val _onBoardingSeen = MutableStateFlow<Boolean?>(null)
    private val _userLogin = MutableStateFlow<String?>(null)
    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    private val _language = MutableStateFlow<String?>(null)

    private val _animationResource = MutableStateFlow<String?>(null)
    val animationResource = _animationResource.asStateFlow()

    init {
        combineFlows()
        getIsDarkTheme()
        getLanguage()
        getUserLogin()
        getOnBoardingSeen()
        getAnimationResource()
    }

    private fun combineFlows() {
        launchWithErrorHandling {
            combine(
                _isDarkTheme,
                _language,
                _userLogin,
                _onBoardingSeen
            ) { isDarkTheme, language, loggedInUser, onBoardingSeen ->
                println("appTAG AppViewModel combineFlows isDarkTheme $isDarkTheme language $language loggedInUser $loggedInUser onBoardingSeen $onBoardingSeen")
                ScreenState(isDarkTheme = isDarkTheme, language = language, userLogin = loggedInUser, isOnboardingSeen = onBoardingSeen)
            }.collect { newState ->
                println("appTAG AppViewModel combineFlows $newState")
                _screenState.value = _screenState.value.copy(
                    isDarkTheme = newState.isDarkTheme,
                    language = newState.language,
                    userLogin = newState.userLogin,
                    isOnboardingSeen = newState.isOnboardingSeen
                )
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    fun getAnimationResource() {
        launchWithErrorHandling {
            val resource = Res.readBytes("files/main_progress.json").decodeToString()
            _animationResource.value = resource
        }
    }

    private fun getUserLogin() {
        launchWithResultHandling {
            userLoginUseCase.get().onSuccess { userLogin ->
                _userLogin.value = userLogin.orEmpty()
            }
        }
    }

    private fun getOnBoardingSeen() {
        launchWithResultHandling {
            onboardingUseCase.get().onSuccess { isOnBoardingSeen ->
                _onBoardingSeen.value = isOnBoardingSeen.isTrue()
            }
        }
    }

    private fun getIsDarkTheme() {
        launchWithResultHandling {
            themeUseCase.get().onSuccess {
                _isDarkTheme.value = it
            }
        }
    }

    private fun getLanguage() {
        launchWithResultHandling {
            languageUseCase.get().onSuccess {
                _language.value = it ?: APP_LANG_EN
            }
        }
    }

    fun updateScreenState(newState: ScreenState) {
        _screenState.value = newState
    }
}