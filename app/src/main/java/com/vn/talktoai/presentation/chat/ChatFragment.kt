package com.vn.talktoai.presentation.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.fragment.app.viewModels
import com.vn.talktoai.CommonExtensions.safeSingleObserve
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.models.Choice
import com.vn.talktoai.domain.models.Message
import com.vn.talktoai.presentation.ChatUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val chatUiState = ChatUiState(listOf(), false)
        setContent {
            ChatContent(chatUiState)   { inputValue ->
                if (inputValue.value.text.isEmpty()) {
                    Log.e("apiTAG", "ChatFragment onCreateView inputValue.value.text.isEmpty()")
                } else {
                    getCompletions(inputValue, chatUiState)
                }
            }
        }
        viewModel.completionLiveData.safeSingleObserve(viewLifecycleOwner) { apiResponse ->
            apiResponse.choices?.forEach {
                chatUiState.addChoice(it)
            }
        }
        viewModel.isProgressProcessLiveData.safeSingleObserve(viewLifecycleOwner) { isLoading ->
            Log.e("apiTAG", "ChatFragment onCreateView isProgressProcessLiveData isLoading $isLoading ")
            chatUiState.changeLoading(isLoading)
        }
    }

    private fun getCompletions(inputValue: MutableState<TextFieldValue>, chatUiState: ChatUiState) {
        val apiRequest = ApiRequest(model = "gpt-3.5-turbo", temperature = 0.7f, messages = listOf(
            Message(role = "user", content = inputValue.value.text)
        ))
        viewModel.getCompletions(apiRequest)
        chatUiState.addChoice(Choice(message = Message(role = "me", content = inputValue.value.text), "reason", 0))
        inputValue.value = TextFieldValue("")
    }
}