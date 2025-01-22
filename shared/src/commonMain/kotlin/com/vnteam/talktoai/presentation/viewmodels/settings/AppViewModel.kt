package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.data.APP_LANG_EN
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.IdTokenUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.LanguageUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.OnboardingUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.ThemeUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import com.vnteam.talktoai.utils.AnimationUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import org.jetbrains.compose.resources.ExperimentalResourceApi

class AppViewModel(
    private val themeUseCase: ThemeUseCase,
    private val languageUseCase: LanguageUseCase,
    private val idTokenUseCase: IdTokenUseCase,
    private val userEmailUseCase: UserEmailUseCase,
    private val onboardingUseCase: OnboardingUseCase,
    val animationUtils: AnimationUtils
) : BaseViewModel() {


    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val _onBoardingSeen = MutableStateFlow<Boolean?>(null)
    private val _idToken = MutableStateFlow<String?>(null)
    private val _userEmail = MutableStateFlow<String?>(null)
    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    private val _language = MutableStateFlow<String?>(null)

    private val _animationResource = MutableStateFlow<String?>(null)
    val animationResource = _animationResource.asStateFlow()

    init {
        observeScreenState()
        fetchInitialData()
    }

    private fun fetchInitialData() {
        fetchAndSet(_isDarkTheme, themeUseCase::get)
        fetchAndSet(_language, languageUseCase::get, APP_LANG_EN)
        fetchAndSet(_idToken, idTokenUseCase::get)
        fetchAndSet(_userEmail, userEmailUseCase::get)
        fetchAndSet(_onBoardingSeen, onboardingUseCase::get) { it.isTrue() }
        loadAnimationResource()
    }

    private fun <T, R> fetchAndSet(
        stateFlow: MutableStateFlow<R?>,
        useCaseCall: suspend () -> Flow<Result<T>>,
        defaultValue: R? = null,
        transform: (T?) -> R? = { it as? R }
    ) {
        launchWithResultHandling {
            useCaseCall().onSuccess { result ->
                stateFlow.value = transform(result) ?: defaultValue
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private fun loadAnimationResource() {
        launchWithErrorHandling {
            val resource = Res.readBytes("files/main_progress.json").decodeToString()
            _animationResource.value = resource
        }
    }

    private fun observeScreenState() {
        launchWithErrorHandling {
            combine(
                _isDarkTheme,
                _language,
                _idToken,
                _userEmail,
                _onBoardingSeen
            ) { isDarkTheme, language, idToken, userEmail, onBoardingSeen ->
                ScreenState(
                    isDarkTheme = isDarkTheme,
                    language = language,
                    idToken = idToken,
                    userEmail = userEmail,
                    isOnboardingSeen = onBoardingSeen
                )
            }.collect { newState ->
                _screenState.value = newState
            }
        }
    }

    fun updateScreenState(newState: ScreenState) {
        _screenState.value = newState
    }
}