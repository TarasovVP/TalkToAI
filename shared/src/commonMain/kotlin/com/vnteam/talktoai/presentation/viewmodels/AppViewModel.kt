package com.vnteam.talktoai.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.vnteam.talktoai.domain.usecase.AppUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val appUseCase: AppUseCase
) : BaseViewModel() {

    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    val isDarkTheme: StateFlow<Boolean?> = _isDarkTheme.asStateFlow()

    private val _language = MutableStateFlow<String?>(null)
    val language: StateFlow<String?> = _language.asStateFlow()

    init {
        getIsDarkTheme()
        getLanguage()
    }

    private fun getIsDarkTheme() {
        viewModelScope.launch {
            appUseCase.getIsDarkTheme().collect {
                _isDarkTheme.value = it
            }
        }
    }

    fun setIsDarkTheme(isDarkTheme: Boolean) {
        showProgress()
        viewModelScope.launch {
            hideProgress()
            appUseCase.setIsDarkTheme(isDarkTheme)
        }
    }

    private fun getLanguage() {
        viewModelScope.launch {
            appUseCase.getLanguage().collect {
                _language.value = it
            }
        }
    }

    fun setLanguage(language: String) {
        showProgress()
        viewModelScope.launch {
            hideProgress()
            appUseCase.setLanguage(language)
        }
    }
}