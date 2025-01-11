package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.RemoteUser
import kotlinx.coroutines.flow.Flow

interface RealDataBaseRepository {

    fun insertRemoteUser(remoteUser: RemoteUser): Flow<NetworkResult<Unit>>

    fun updateRemoteUser(remoteUser: RemoteUser, result: (NetworkResult<Unit>) -> Unit)

    fun deleteRemoteUser(result: (NetworkResult<Unit>) -> Unit)

    fun updateRemoteChats(chats: List<Chat>, result: (NetworkResult<Unit>) -> Unit)

    fun addRemoteChatListener()

    fun addRemoteMessageListener()

    fun removeRemoteChatListener()

    fun removeRemoteMessageListener()

    fun insertChat(chat: Chat, result: (NetworkResult<Unit>) -> Unit)

    fun updateChat(chat: Chat, result: (NetworkResult<Unit>) -> Unit)

    fun deleteChat(chat: Chat, result: (NetworkResult<Unit>) -> Unit)

    fun insertMessage(message: Message, result: (NetworkResult<Unit>) -> Unit)

    fun deleteMessages(messageIds: List<String>, result: (NetworkResult<Unit>) -> Unit)

    fun deleteMessagesByChatId(chatId: Long, result: (NetworkResult<Unit>) -> Unit)

    fun setReviewVoted(result: (NetworkResult<Unit>) -> Unit)

    fun insertFeedback(feedback: Feedback, result: (NetworkResult<Unit>) -> Unit)

    fun getPrivacyPolicy(appLang: String, result: (NetworkResult<String>) -> Unit)
}