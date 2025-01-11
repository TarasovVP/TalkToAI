package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.data.APP_LANG_EN
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.usecase.AppUseCase
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.utils.AnimationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import org.jetbrains.compose.resources.ExperimentalResourceApi

class AppViewModel(
    private val appUseCase: AppUseCase,
    val animationUtils: AnimationUtils
) : BaseViewModel() {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val _onBoardingSeen = MutableStateFlow<Boolean?>(null)
    private val _loggedInUser = MutableStateFlow<Boolean?>(null)
    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    private val _language = MutableStateFlow<String?>(null)

    private val _animationResource = MutableStateFlow<String?>(null)
    val animationResource = _animationResource.asStateFlow()

    init {
        combineFlows()
        getIsDarkTheme()
        getLanguage()
        getIsLoggedInUser()
        getOnBoardingSeen()
        getAnimationResource()
    }

    private fun combineFlows() {
        launchWithErrorHandling {
            combine(
                _isDarkTheme,
                _language,
                _loggedInUser,
                _onBoardingSeen
            ) { isDarkTheme, language, loggedInUser, onBoardingSeen ->
                println("appTAG AppViewModel combineFlows isDarkTheme $isDarkTheme language $language loggedInUser $loggedInUser onBoardingSeen $onBoardingSeen")
                ScreenState(isDarkTheme = isDarkTheme, language = language, isLoggedInUser = loggedInUser, isOnboardingSeen = onBoardingSeen)
            }.collect { newState ->
                println("appTAG AppViewModel combineFlows $newState")
                _screenState.value = _screenState.value.copy(
                    isDarkTheme = newState.isDarkTheme,
                    language = newState.language,
                    isLoggedInUser = newState.isLoggedInUser,
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

    private fun getIsLoggedInUser() {
        launchWithResultHandling {
            appUseCase.getIsLoggedInUser().onSuccess { isLoggedInUser ->
                _loggedInUser.value = isLoggedInUser.isTrue()
            }
        }
    }

    private fun getOnBoardingSeen() {
        launchWithResultHandling {
            appUseCase.getIsBoardingSeen().onSuccess { isOnBoardingSeen ->
                _onBoardingSeen.value = isOnBoardingSeen.isTrue()
            }
        }
    }

    private fun getIsDarkTheme() {
        launchWithResultHandling {
            appUseCase.getIsDarkTheme().onSuccess {
                _isDarkTheme.value = it
            }
        }
    }

    fun setIsDarkTheme(isDarkTheme: Boolean) {
        showProgress()
        launchWithErrorHandling {
            hideProgress()
            appUseCase.setIsDarkTheme(isDarkTheme)
        }
    }

    private fun getLanguage() {
        launchWithResultHandling {
            appUseCase.getLanguage().onSuccess {
                _language.value = it ?: APP_LANG_EN
            }
        }
    }

    fun setLanguage(language: String) {
        showProgress()
        launchWithErrorHandling {
            hideProgress()
            appUseCase.setLanguage(language)
        }
    }

    fun updateScreenState(newState: ScreenState) {
        _screenState.value = newState
    }
}