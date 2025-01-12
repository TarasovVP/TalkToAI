package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.RemoteUser
import kotlinx.coroutines.flow.Flow

interface RealDataBaseRepository {

    fun insertRemoteUser(remoteUser: RemoteUser): Flow<Result<Unit>>

    fun updateRemoteUser(remoteUser: RemoteUser): Flow<Result<Unit>>

    fun deleteRemoteUser(): Flow<Result<Unit>>

    fun updateRemoteChats(chats: List<Chat>): Flow<Result<Unit>>

    fun addRemoteChatListener()

    fun addRemoteMessageListener()

    fun removeRemoteChatListener()

    fun removeRemoteMessageListener()

    fun insertChat(chat: Chat): Flow<Result<Unit>>

    fun updateChat(chat: Chat): Flow<Result<Unit>>

    fun deleteChat(chat: Chat): Flow<Result<Unit>>

    fun insertMessage(message: Message): Flow<Result<Unit>>

    fun deleteMessages(messageIds: List<String>): Flow<Result<Unit>>

    fun deleteMessagesByChatId(chatId: Long): Flow<Result<Unit>>

    fun setReviewVoted(): Flow<Result<Unit>>

    fun insertFeedback(feedback: Feedback): Flow<Result<Unit>>

    fun getPrivacyPolicy(appLang: String): Flow<Result<String>>
}