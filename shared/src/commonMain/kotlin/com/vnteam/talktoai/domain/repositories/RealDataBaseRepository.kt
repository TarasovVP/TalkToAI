package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.RemoteUser
import kotlinx.coroutines.flow.Flow

interface RealDataBaseRepository {

    fun insertRemoteUser(remoteUser: RemoteUser): Flow<NetworkResult<Unit>>

    fun updateRemoteUser(remoteUser: RemoteUser): Flow<NetworkResult<Unit>>

    fun deleteRemoteUser(): Flow<NetworkResult<Unit>>

    fun updateRemoteChats(chats: List<Chat>): Flow<NetworkResult<Unit>>

    fun addRemoteChatListener()

    fun addRemoteMessageListener()

    fun removeRemoteChatListener()

    fun removeRemoteMessageListener()

    fun insertChat(chat: Chat): Flow<NetworkResult<Unit>>

    fun updateChat(chat: Chat): Flow<NetworkResult<Unit>>

    fun deleteChat(chat: Chat): Flow<NetworkResult<Unit>>

    fun insertMessage(message: Message): Flow<NetworkResult<Unit>>

    fun deleteMessages(messageIds: List<String>): Flow<NetworkResult<Unit>>

    fun deleteMessagesByChatId(chatId: Long): Flow<NetworkResult<Unit>>

    fun setReviewVoted(): Flow<NetworkResult<Unit>>

    fun insertFeedback(feedback: Feedback): Flow<NetworkResult<Unit>>

    fun getPrivacyPolicy(appLang: String): Flow<NetworkResult<String>>
}