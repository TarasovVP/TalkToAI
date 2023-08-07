package com.vn.talktoai.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import com.vn.talktoai.domain.models.Choice

data class ChatUiState(var choiceList: List<Choice>, var loading: Boolean) {
    private val choiceStateList: MutableList<Choice> = choiceList.toMutableStateList()
    val choices: List<Choice> = choiceStateList

    private var isLoadingState = mutableStateOf(loading)
    val isLoadingProgress: Boolean = isLoadingState.value

    fun addChoice(choice: Choice) {
        choiceStateList.add(choice)
    }

    fun changeLoading(isLoading: Boolean) {
        loading = isLoading
    }
}
