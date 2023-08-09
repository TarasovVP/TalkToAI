package com.vn.talktoai.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import com.vn.talktoai.domain.models.Choice

data class ChatUiState(var choiceList: List<Choice>, var loading: Boolean) {
    private val choiceStateList: MutableList<Choice> = choiceList.toMutableStateList()
    val choices: List<Choice> = choiceStateList

    var isLoadingState = mutableStateOf(loading)

    fun addChoices(choice: List<Choice>) {
        choiceStateList.addAll(choice)
    }
}
