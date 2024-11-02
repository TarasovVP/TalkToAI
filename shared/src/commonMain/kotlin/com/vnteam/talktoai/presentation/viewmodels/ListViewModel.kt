package com.vnteam.talktoai.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.vnteam.talktoai.presentation.mappers.DemoObjectUIMapper
import com.vnteam.talktoai.domain.usecase.ListUseCase
import com.vnteam.talktoai.presentation.intents.ListIntent
import com.vnteam.talktoai.presentation.states.ListViewState
import com.vnteam.talktoai.presentation.states.screen.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel(
    private val listUseCase: ListUseCase,
    private val demoObjectUIMapper: DemoObjectUIMapper,
    screenState: MutableState<ScreenState>
) : BaseViewModel(screenState) {

    private val _state = MutableStateFlow(ListViewState())
    val state: StateFlow<ListViewState> = _state.asStateFlow()

    fun processIntent(intent: ListIntent) {
        when (intent) {
            is ListIntent.ClearDemoObjects -> clearDemoObjects()
            is ListIntent.LoadDemoObjects -> getDemoObjectsFromApi(intent.isInit)
            is ListIntent.DeleteDemoObject -> deleteDemoObjectById(intent.id)
        }
    }

    private fun clearDemoObjects() {
        viewModelScope.launch(exceptionHandler) {
            listUseCase.clearChats()
            getDemoObjectsFromApi(true)
        }
    }

    private fun getDemoObjectsFromApi(isInit: Boolean) {
        if (isInit) showProgress(true)
        viewModelScope.launch(exceptionHandler) {
            listUseCase.getDemoObjectsFromApi().collect { demoObjects ->
                insertDemoObjectsToDB(demoObjects)
            }
        }
    }

    private fun insertDemoObjectsToDB(chats: List<Chat>?) {
        viewModelScope.launch(exceptionHandler) {
            chats?.let {
                listUseCase.insertDemoObjectsToDB(it).collect {
                    getDemoObjectsFromDB()
                }
            }
        }
    }

    private fun getDemoObjectsFromDB() {
        viewModelScope.launch(exceptionHandler) {
            listUseCase.getDemoObjectsFromDB().collect {
                val demoObjects = demoObjectUIMapper.mapToImplModelList(it)
                _state.value = state.value.copy(demoObjectUIs = demoObjects)
                showProgress(false)
            }
        }
    }

    private fun deleteDemoObjectById(demoObjectId: String) {
        showProgress(true)
        _state.value = state.value.copy(successResult = false)
        viewModelScope.launch(exceptionHandler) {
            listUseCase.deleteDemoObjectById(demoObjectId).collect {
                _state.value = state.value.copy(successResult = true)
                showMessage("Successfully deleted", false)
                showProgress(false)
            }
        }
    }
}