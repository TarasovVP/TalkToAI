package com.vnstudio.talktoai.presentation.screens.main

import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.domain.repositories.*
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.MainUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
) : MainUseCase {

    override suspend fun getOnBoardingSeen(): Flow<Boolean?> {
        return dataStoreRepository.onBoardingSeen()
    }

    override fun isLoggedInUser(): Boolean {
        return authRepository.isLoggedInUser()
    }

    override fun getRemoteUser(result: (Result<RemoteUser>) -> Unit) =
        realDataBaseRepository.getRemoteUser { operationResult ->
            result.invoke(operationResult)
        }

    override suspend fun setReviewVoted(isReviewVoted: Boolean) =
        dataStoreRepository.setReviewVoted(isReviewVoted)

    override suspend fun insertChats(chats: List<Chat>) = chatRepository.insertChats(chats)

    override suspend fun insertMessages(messages: List<Message>) = messageRepository.insertMessages(messages)

    override suspend fun getChats(): Flow<List<Chat>> = chatRepository.getChats()

    override suspend fun updateChats(chats: List<Chat>) = chatRepository.updateChats(chats)

    override suspend fun insertChat(chat: Chat) = chatRepository.insertChat(chat)

    override suspend fun updateChat(chat: Chat) = chatRepository.updateChat(chat)

    override suspend fun deleteChat(chat: Chat) {
        chatRepository.deleteChat(chat)
        messageRepository.deleteMessagesFromChat(chat.id)
    }
}