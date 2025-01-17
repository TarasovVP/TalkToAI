package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.usecase.execute
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.DeleteChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.GetChatsUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.InsertChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.UpdateChatUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.UpdateChatsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatListViewModel(
    private val getChatsUseCase: GetChatsUseCase,
    private val insertChatUseCase: InsertChatUseCase,
    private val updateChatUseCase: UpdateChatUseCase,
    private val updateChatsUseCase: UpdateChatsUseCase,
    private val deleteChatUseCase: DeleteChatUseCase
) : BaseViewModel() {

    private val _chatsList = MutableStateFlow<List<Chat>?>(null)
    val chatsList = _chatsList.asStateFlow()
    private val _animationResource = MutableStateFlow<String?>(null)
    val animationResource = _animationResource.asStateFlow()

    fun getChats() {
        launchWithResultHandling {
            getChatsUseCase.execute().onSuccess { chats ->
                println("flowTAG ChatListViewModel getChats chats: $chats")
                _chatsList.value = chats
                hideProgress()
            }
        }
    }

    fun updateChats(chats: List<Chat>) {
        launchWithResult {
            updateChatsUseCase.execute(chats)
        }
    }

    fun insertChat(chat: Chat) {
        launchWithResult {
            insertChatUseCase.execute(chat)
        }
    }

    fun updateChat(chat: Chat) {
        launchWithResult {
            updateChatUseCase.execute(chat)
        }
    }

    fun deleteChat(chat: Chat) {
        launchWithResult {
            deleteChatUseCase.execute(chat)
        }
    }

    fun swapChats(firstIndex: Int, secondIndex: Int) {
        val fromItem = _chatsList.value?.get(firstIndex)
        val toItem = _chatsList.value?.get(secondIndex)
        val newList = _chatsList.value.orEmpty().toMutableList()
        toItem?.let {
            newList[firstIndex] = it.apply {
                listOrder = (_chatsList.value.orEmpty().size - firstIndex).toLong()
            }
        }
        fromItem?.let {
            newList[secondIndex] = it.apply {
                listOrder = (_chatsList.value.orEmpty().size - secondIndex).toLong()
            }
        }
        _chatsList.value = null
        _chatsList.value = newList
    }
}