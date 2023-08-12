package com.vn.talktoai.presentation.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.vn.talktoai.CommonExtensions.EMPTY
import com.vn.talktoai.CommonExtensions.safeSingleObserve
import com.vn.talktoai.data.database.db_entities.Message
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.models.MessageApi
import com.vn.talktoai.presentation.uistates.ChatUiState
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()

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
                    viewModel.sendRequest(ApiRequest(model = "gpt-3.5-turbo", temperature = 0.7f, messages = listOf(
                        MessageApi(role = "user", content = inputValue.value.text)
                    )))
                    viewModel.insertMessage(Message(chatId = args.chatId, author = "me", message = inputValue.value.text, createdAt = Date().time))
                    inputValue.value = TextFieldValue(String.EMPTY)
                }
            }
        }
        viewModel.getMessagesFromChat(args.chatId)
        viewModel.messagesLiveData.safeSingleObserve(viewLifecycleOwner) { messages ->
            chatUiState.clearMessages()
            messages.forEach {
                chatUiState.addMessage(it)
            }
        }
        viewModel.sendRequestLiveData.safeSingleObserve(viewLifecycleOwner) { apiResponse ->
            viewModel.insertMessage(Message(chatId = args.chatId, author = apiResponse.model.orEmpty(), message = apiResponse.choices?.firstOrNull()?.message?.content.orEmpty(), createdAt = apiResponse.created?.toLongOrNull() ?: 0))
        }
        viewModel.isProgressProcessLiveData.safeSingleObserve(viewLifecycleOwner) { isLoading ->
            chatUiState.isLoadingState.value = isLoading
            Log.e("apiTAG", "ChatFragment onCreateView isProgressProcessLiveData isLoading $isLoading isLoadingProgress ${chatUiState.isLoadingState.value}")
        }
    }
}