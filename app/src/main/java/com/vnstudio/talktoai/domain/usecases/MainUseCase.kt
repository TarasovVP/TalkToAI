package com.vnstudio.talktoai.domain.usecases

import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.domain.sealed_classes.Result
import kotlinx.coroutines.flow.Flow

interface MainUseCase {

    suspend fun getOnBoardingSeen(): Flow<Boolean?>

    fun isLoggedInUser(): Boolean

    fun getRemoteUser(result: (Result<RemoteUser>) -> Unit)

    suspend fun setReviewVoted(isReviewVoted: Boolean)

    suspend fun insertChats(chats: List<Chat>)

    suspend fun insertMessages(messages: List<Message>)

    suspend fun getChats(): Flow<List<Chat>>

    suspend fun updateChats(chats: List<Chat>)

    suspend fun insertChat(chat: Chat)

    suspend fun updateChat(chat: Chat)

    suspend fun deleteChat(chat: Chat)
}