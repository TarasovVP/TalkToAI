package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.aimodels.RawAiModelsConfig
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.MessageContent
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.SyncRemoteSettingsUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.SyncRemoteUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

private class FakeMessageRepo(private var messages: List<Message>) : MessageRepository {
    var inserted: List<Message> = emptyList()
    override suspend fun insertMessages(messages: List<Message>) { inserted = messages }
    override suspend fun insertMessage(message: Message) = TODO()
    override suspend fun getMessages(): Flow<List<Message>> = flowOf(messages)
    override suspend fun getMessagesFromChat(chatId: Long) = TODO()
    override suspend fun getMessagesByIds(ids: List<Long>) = TODO()
    override suspend fun deleteMessage(id: Long) = TODO()
    override suspend fun deleteMessages(messageIds: List<Long>) = TODO()
    override suspend fun deleteMessagesFromChat(chatId: Long) = TODO()
    override suspend fun updateMessages(messages: List<Message>) = TODO()
    override suspend fun clearMessages() { messages = emptyList() }
}

private class FakeChatRepo : ChatRepository {
    override suspend fun clearChats() {}
    override suspend fun getChatById(chatId: String) = TODO()
    override suspend fun insertChats(chats: List<Chat>) {}
    override suspend fun insertChat(chat: Chat) = TODO()
    override suspend fun getChats() = TODO()
    override suspend fun getLastUpdatedChat() = TODO()
    override suspend fun updateChat(chat: Chat) = TODO()
    override suspend fun deleteChat(chat: Chat) = TODO()
    override suspend fun updateChats(chats: List<Chat>) = TODO()
}

private class FakeRemoteStore(private val remoteUser: RemoteUser) : RemoteStoreRepository {
    override fun getRemoteUser(): Flow<Result<RemoteUser>> = flowOf(Result.Success(remoteUser))
    override fun getRemoteSettings(): Flow<Result<Map<String, String?>>> = flowOf(Result.Success(emptyMap()))

    override fun insertRemoteUser(remoteUser: RemoteUser) = TODO()
    override fun updateRemoteUser(remoteUser: RemoteUser) = TODO()
    override fun deleteRemoteUser() = TODO()
    override fun updateRemoteChats(chats: List<Chat>) = TODO()
    override fun addRemoteChatListener() = TODO()
    override fun addRemoteMessageListener() = TODO()
    override fun removeRemoteChatListener() = TODO()
    override fun removeRemoteMessageListener() = TODO()
    override fun insertChat(chat: Chat) = TODO()
    override fun updateChat(chat: Chat) = TODO()
    override fun deleteChat(chat: Chat) = TODO()
    override fun insertMessage(message: Message) = TODO()
    override fun deleteMessages(messageIds: List<String>) = TODO()
    override fun deleteMessagesByChatId(chatId: Long) = TODO()
    override fun setReviewVoted() = TODO()
    override fun getPrivacyPolicy(appLang: String) = TODO()
    override fun getAiModelsConfig(): Flow<Result<RawAiModelsConfig>> = TODO()
    override fun setRemoteSettings(settings: Map<String, String?>) = TODO()
}

private class FakeSyncPreferences : PreferencesRepository {
    override fun getIsDarkTheme() = TODO()
    override suspend fun setIsDarkTheme(isDarkTheme: Boolean) = TODO()
    override fun getLanguage() = TODO()
    override suspend fun setLanguage(language: String) = TODO()
    override fun getIsBoardingSeen() = TODO()
    override suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean) = TODO()
    override fun getUserEmail() = TODO()
    override suspend fun setUserEmail(userEmail: String) = TODO()
    override fun getIdToken() = TODO()
    override suspend fun setIdToken(idToken: String) = TODO()
    override fun getIsReviewVoted() = TODO()
    override suspend fun setReviewVoted(isReviewVoted: Boolean) = TODO()
    override fun getAiTier() = TODO()
    override suspend fun setAiTier(tier: String) = TODO()
    override fun getAiProvider() = TODO()
    override suspend fun setAiProvider(provider: String) = TODO()
    override fun getAiModelsCache() = TODO()
    override suspend fun setAiModelsCache(json: String) = TODO()
    override fun getAiModelsFetchedAt() = TODO()
    override suspend fun setAiModelsFetchedAt(timestamp: String) = TODO()
    override fun getGlobalSystemContext() = TODO()
    override suspend fun setGlobalSystemContext(context: String) = TODO()
    override fun getUid() = TODO()
    override suspend fun setUid(uid: String) = TODO()
    override fun getRefreshToken() = TODO()
    override suspend fun setRefreshToken(refreshToken: String) = TODO()
}

class SyncRemoteUserUseCaseTest {

    @Test
    fun executeRestoresLocalImageStorageKeyLostInRemoteRoundTrip() = runTest {
        val localImage = MessageContent.Image(mimeType = "image/png", storageKey = "key.png")
        val localMessages = listOf(
            Message(id = 1L, chatId = 10L, content = listOf(MessageContent.Text("hi"), localImage)),
        )
        // Remote documents never carry storageKey - this simulates what toMessage() produces:
        // text-only content, even though the same message locally has an attached image.
        val remoteMessages = arrayListOf(
            Message(id = 1L, chatId = 10L, content = listOf(MessageContent.Text("hi"))),
        )
        val messageRepo = FakeMessageRepo(localMessages)
        val useCase = SyncRemoteUserUseCase(
            remoteStoreRepository = FakeRemoteStore(RemoteUser(messages = remoteMessages)),
            chatRepository = FakeChatRepo(),
            messageRepository = messageRepo,
            syncRemoteSettingsUseCase = SyncRemoteSettingsUseCase(
                FakeRemoteStore(RemoteUser()),
                FakeSyncPreferences(),
            ),
        )

        val result = useCase.execute()

        assertIs<Result.Success<RemoteUser>>(result)
        val synced = messageRepo.inserted.single()
        assertEquals(2, synced.content.size)
        val image = assertIs<MessageContent.Image>(synced.content[1])
        assertEquals("key.png", image.storageKey)
    }

    @Test
    fun executeLeavesMessagesWithoutALocalImageUntouched() = runTest {
        val remoteMessages = arrayListOf(
            Message(id = 2L, chatId = 10L, content = listOf(MessageContent.Text("no image here"))),
        )
        val messageRepo = FakeMessageRepo(emptyList())
        val useCase = SyncRemoteUserUseCase(
            remoteStoreRepository = FakeRemoteStore(RemoteUser(messages = remoteMessages)),
            chatRepository = FakeChatRepo(),
            messageRepository = messageRepo,
            syncRemoteSettingsUseCase = SyncRemoteSettingsUseCase(
                FakeRemoteStore(RemoteUser()),
                FakeSyncPreferences(),
            ),
        )

        useCase.execute()

        val synced = messageRepo.inserted.single()
        assertEquals(1, synced.content.size)
        assertNull(synced.content.filterIsInstance<MessageContent.Image>().firstOrNull())
    }
}
