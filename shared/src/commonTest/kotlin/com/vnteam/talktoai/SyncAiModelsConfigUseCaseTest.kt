package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.aimodels.AiModelsCacheCodec
import com.vnteam.talktoai.domain.aimodels.AiModelsConfigResult
import com.vnteam.talktoai.domain.aimodels.AiModelsConfigValidator
import com.vnteam.talktoai.domain.aimodels.RawAiModelsConfig
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.models.AiModels
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.SyncAiModelsConfigUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private class FakePreferences(var cache: String? = null, var fetchedAt: String? = null) : PreferencesRepository {
    override fun getAiModelsCache(): Flow<String?> = flowOf(cache)
    override suspend fun setAiModelsCache(json: String) { cache = json }
    override fun getAiModelsFetchedAt(): Flow<String?> = flowOf(fetchedAt)
    override suspend fun setAiModelsFetchedAt(timestamp: String) { fetchedAt = timestamp }

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
    override fun getGlobalSystemContext() = TODO()
    override suspend fun setGlobalSystemContext(context: String) = TODO()
    override fun getUid() = TODO()
    override suspend fun setUid(uid: String) = TODO()
    override fun getRefreshToken() = TODO()
    override suspend fun setRefreshToken(refreshToken: String) = TODO()
}

private class FakeRemote(var response: Result<RawAiModelsConfig>) : RemoteStoreRepository {
    var calls = 0
    override fun getAiModelsConfig(): Flow<Result<RawAiModelsConfig>> {
        calls++
        return flowOf(response)
    }

    override fun insertRemoteUser(remoteUser: RemoteUser) = TODO()
    override fun updateRemoteUser(remoteUser: RemoteUser) = TODO()
    override fun getRemoteUser() = TODO()
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
    override fun getRemoteSettings() = TODO()
    override fun setRemoteSettings(settings: Map<String, String?>) = TODO()
}

class SyncAiModelsConfigUseCaseTest {

    private val now = 10L * 24 * 60 * 60 * 1000
    private val logs = mutableListOf<String>()

    @AfterTest
    fun resetRegistry() = AiModels.reset()

    private fun useCase(remote: FakeRemote, prefs: FakePreferences) =
        SyncAiModelsConfigUseCase(remote, prefs, now = { now }, log = { logs += it })

    private fun serverConfig(openAiId: String = "gpt-server") = RawAiModelsConfig(
        1,
        listOf(raw(openAiId), raw("claude-server", "ANTHROPIC")),
    )

    private fun cacheOf(config: RawAiModelsConfig): String {
        val valid = AiModelsConfigValidator.validate(config) as AiModelsConfigResult.Valid
        return AiModelsCacheCodec.encode(valid.schemaVersion, valid.models)
    }

    private fun openAiIds() = AiModels.forProvider(AiProviderType.OPENAI).map { it.id }

    @Test
    fun validServerConfigReplacesListAndUpdatesCacheAndTimestamp() = runTest {
        val prefs = FakePreferences()
        useCase(FakeRemote(Result.Success(serverConfig())), prefs).refreshIfDue()
        assertEquals(listOf("gpt-server"), openAiIds())
        assertEquals(now.toString(), prefs.fetchedAt)
        assertEquals(cacheOf(serverConfig()), prefs.cache)
    }

    @Test
    fun networkErrorUsesCacheWhenPresent() = runTest {
        val prefs = FakePreferences(cache = cacheOf(serverConfig("gpt-cached")))
        val useCase = useCase(FakeRemote(Result.Failure("offline")), prefs)
        useCase.applyCached()
        useCase.refreshIfDue()
        assertEquals(listOf("gpt-cached"), openAiIds())
    }

    @Test
    fun networkErrorWithoutCacheKeepsHardcode() = runTest {
        val useCase = useCase(FakeRemote(Result.Failure("offline")), FakePreferences())
        useCase.applyCached()
        useCase.refreshIfDue()
        assertEquals(AiModels.OPENAI.map { it.id }, openAiIds())
    }

    @Test
    fun failedFetchDoesNotOverwriteValidCacheOrTimestamp() = runTest {
        val cache = cacheOf(serverConfig("gpt-cached"))
        val prefs = FakePreferences(cache = cache, fetchedAt = null)
        val remote = FakeRemote(Result.Failure("offline"))
        useCase(remote, prefs).refreshIfDue()
        assertEquals(cache, prefs.cache)
        assertNull(prefs.fetchedAt)
    }

    @Test
    fun rejectedServerConfigDoesNotOverwriteCache() = runTest {
        val cache = cacheOf(serverConfig("gpt-cached"))
        val prefs = FakePreferences(cache = cache)
        useCase(FakeRemote(Result.Success(RawAiModelsConfig(3, serverConfig().models))), prefs).refreshIfDue()
        assertEquals(cache, prefs.cache)
        assertNull(prefs.fetchedAt)
    }

    @Test
    fun cacheWithNewerSchemaIsIgnoredAndHardcodeUsed() = runTest {
        val newer = AiModelsCacheCodec.encode(1, (AiModelsConfigValidator.validate(serverConfig()) as AiModelsConfigResult.Valid).models)
            .replace("\"schemaVersion\":1", "\"schemaVersion\":3")
        val useCase = useCase(FakeRemote(Result.Failure("offline")), FakePreferences(cache = newer))
        useCase.applyCached()
        assertEquals(AiModels.OPENAI.map { it.id }, openAiIds())
    }

    @Test
    fun recentFetchStillTriggersRefresh() = runTest {
        val prefs = FakePreferences(cache = cacheOf(serverConfig()), fetchedAt = (now - 1000).toString())
        val remote = FakeRemote(Result.Success(serverConfig("gpt-new")))
        useCase(remote, prefs).refreshIfDue()
        assertEquals(1, remote.calls)
        assertEquals(listOf("gpt-new"), openAiIds())
    }
}
