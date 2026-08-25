package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.Constants.CHATS
import com.vnteam.talktoai.Constants.MESSAGES
import com.vnteam.talktoai.Constants.PRIVACY_POLICY
import com.vnteam.talktoai.Constants.SETTINGS
import com.vnteam.talktoai.Constants.USERS
import com.vnteam.talktoai.data.ERROR_FIRESTORE_DELETE_CHAT_FAILED
import com.vnteam.talktoai.data.ERROR_FIRESTORE_WRITE_FAILED
import com.vnteam.talktoai.data.ERROR_NOT_AUTHENTICATED
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.firestore.FirestoreCollectionSelector
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_AI_MODEL
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_API_KEY
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_AUTHOR
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_CHAT_ID
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_CONTEXT
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_ERROR_MESSAGE
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_GLOBAL_CONTEXT
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_ID
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_LIST_ORDER
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_MESSAGE
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_NAME
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_STATUS
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_TEMPERATURE
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_TEXT
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_TRUNCATED
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_UPDATED
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_UPDATED_AT
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_VOTED
import com.vnteam.talktoai.data.network.firestore.FirestoreDocument
import com.vnteam.talktoai.data.network.firestore.FirestoreFieldFilter
import com.vnteam.talktoai.data.network.firestore.FirestoreFieldReference
import com.vnteam.talktoai.data.network.firestore.FirestoreFilter
import com.vnteam.talktoai.data.network.firestore.FirestoreQuery
import com.vnteam.talktoai.data.network.firestore.FirestoreService
import com.vnteam.talktoai.data.network.firestore.FirestoreStructuredQuery
import com.vnteam.talktoai.data.network.firestore.FirestoreValue
import com.vnteam.talktoai.data.network.firestore.firestoreBool
import com.vnteam.talktoai.data.network.firestore.firestoreDouble
import com.vnteam.talktoai.data.network.firestore.firestoreInt
import com.vnteam.talktoai.data.network.firestore.firestoreString
import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class RemoteStoreRepositoryImpl(
    private val firestoreService: FirestoreService,
    private val preferencesRepository: PreferencesRepository,
) : RemoteStoreRepository {

    private suspend fun idToken(): String {
        val token = preferencesRepository.getIdToken().firstOrNull().orEmpty()
        return token
    }

    private suspend fun uid(): String {
        val uid = preferencesRepository.getUid().firstOrNull().orEmpty()
        return uid
    }

    private fun userChatsPath(uid: String) = "$USERS/$uid/$CHATS"
    private fun userMessagesPath(uid: String) = "$USERS/$uid/$MESSAGES"
    private fun userSettingsPath(uid: String) = "$USERS/$uid/$SETTINGS/${FirestoreConstants.PATH_GLOBAL}"

    // ---- Chat extensions ----

    private fun Chat.toFields(): Map<String, FirestoreValue> = mapOf(
        FIELD_ID to firestoreInt(id),
        FIELD_NAME to firestoreString(name),
        FIELD_UPDATED to firestoreInt(updated),
        FIELD_LIST_ORDER to firestoreInt(listOrder),
        FIELD_AI_MODEL to firestoreString(aiModel),
        FIELD_TEMPERATURE to firestoreDouble(temperature?.toDouble()),
        FIELD_CONTEXT to firestoreString(context),
    )

    private fun FirestoreDocument.toChat(): Chat? {
        val f = fields ?: return null
        return Chat(
            id = f[FIELD_ID]?.integerValue?.toLongOrNull(),
            name = f[FIELD_NAME]?.stringValue,
            updated = f[FIELD_UPDATED]?.integerValue?.toLongOrNull(),
            listOrder = f[FIELD_LIST_ORDER]?.integerValue?.toLongOrNull(),
            aiModel = f[FIELD_AI_MODEL]?.stringValue,
            temperature = f[FIELD_TEMPERATURE]?.doubleValue?.toFloat(),
            context = f[FIELD_CONTEXT]?.stringValue,
        )
    }

    // ---- Message extensions ----

    private fun Message.toFields(): Map<String, FirestoreValue> = mapOf(
        FIELD_ID to firestoreInt(id),
        FIELD_CHAT_ID to firestoreInt(chatId),
        FIELD_AUTHOR to firestoreString(author),
        FIELD_MESSAGE to firestoreString(message),
        FIELD_UPDATED_AT to firestoreInt(updatedAt),
        FIELD_STATUS to firestoreString(status?.name),
        FIELD_ERROR_MESSAGE to firestoreString(errorMessage),
        FIELD_TRUNCATED to firestoreBool(truncated),
    )

    private fun FirestoreDocument.toMessage(): Message? {
        val f = fields ?: return null
        return Message(
            id = f[FIELD_ID]?.integerValue?.toLongOrNull(),
            chatId = f[FIELD_CHAT_ID]?.integerValue?.toLongOrNull(),
            author = f[FIELD_AUTHOR]?.stringValue,
            message = f[FIELD_MESSAGE]?.stringValue,
            updatedAt = f[FIELD_UPDATED_AT]?.integerValue?.toLongOrNull(),
            status = f[FIELD_STATUS]?.stringValue?.let {
                runCatching { MessageStatus.valueOf(it) }.getOrNull()
            },
            errorMessage = f[FIELD_ERROR_MESSAGE]?.stringValue.orEmpty(),
            truncated = f[FIELD_TRUNCATED]?.booleanValue ?: false,
        )
    }

    // ---- Repository methods ----

    override fun insertRemoteUser(remoteUser: RemoteUser): Flow<Unit> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) return@flow
        remoteUser.chats.forEach { chat ->
            firestoreService.setDocument("${userChatsPath(uid)}/${chat.id}", chat.toFields(), token)
        }
        remoteUser.messages.forEach { message ->
            firestoreService.setDocument(
                "${userMessagesPath(uid)}/${message.id}",
                message.toFields(),
                token
            )
        }
        emit(Unit)
    }

    override fun updateRemoteUser(remoteUser: RemoteUser): Flow<Unit> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) return@flow
        remoteUser.chats.forEach { chat ->
            firestoreService.setDocument("${userChatsPath(uid)}/${chat.id}", chat.toFields(), token)
        }
        remoteUser.messages.forEach { message ->
            firestoreService.setDocument(
                "${userMessagesPath(uid)}/${message.id}",
                message.toFields(),
                token
            )
        }
        emit(Unit)
    }

    override fun getRemoteUser(): Flow<Result<RemoteUser>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure(ERROR_NOT_AUTHENTICATED))
            return@flow
        }

        val chatsResult = firestoreService.listDocuments(userChatsPath(uid), token)
        if (chatsResult is Result.Failure) {
            emit(chatsResult)
            return@flow
        }
        val messagesResult = firestoreService.listDocuments(userMessagesPath(uid), token)
        if (messagesResult is Result.Failure) {
            emit(messagesResult)
            return@flow
        }
        val chats = (chatsResult as Result.Success).data.mapNotNull { it.toChat() }
        val messages = (messagesResult as Result.Success).data.mapNotNull { it.toMessage() }
        emit(Result.Success(RemoteUser(chats = ArrayList(chats), messages = ArrayList(messages))))
    }

    override fun updateRemoteChats(chats: List<Chat>): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure(ERROR_NOT_AUTHENTICATED))
            return@flow
        }
        chats.forEach { chat ->
            firestoreService.setDocument("${userChatsPath(uid)}/${chat.id}", chat.toFields(), token)
        }
        emit(Result.Success(Unit))
    }

    override fun deleteRemoteUser(): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure(ERROR_NOT_AUTHENTICATED))
            return@flow
        }
        val chats = (firestoreService.listDocuments(userChatsPath(uid), token) as? Result.Success)?.data.orEmpty()
        chats.forEach { doc ->
            doc.name?.substringAfterLast('/')?.let { id ->
                firestoreService.deleteDocument("${userChatsPath(uid)}/$id", token)
            }
        }
        val messages = (firestoreService.listDocuments(userMessagesPath(uid), token) as? Result.Success)?.data.orEmpty()
        messages.forEach { doc ->
            doc.name?.substringAfterLast('/')?.let { id ->
                firestoreService.deleteDocument("${userMessagesPath(uid)}/$id", token)
            }
        }
        emit(Result.Success(Unit))
    }

    override fun addRemoteChatListener() = Unit
    override fun addRemoteMessageListener() = Unit
    override fun removeRemoteChatListener() = Unit
    override fun removeRemoteMessageListener() = Unit

    override fun insertChat(chat: Chat): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure(ERROR_NOT_AUTHENTICATED))
            return@flow
        }
        val ok =
            firestoreService.setDocument("${userChatsPath(uid)}/${chat.id}", chat.toFields(), token)
        if (ok) emit(Result.Success(Unit)) else emit(Result.Failure(ERROR_FIRESTORE_WRITE_FAILED))
    }

    override fun updateChat(chat: Chat): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure(ERROR_NOT_AUTHENTICATED))
            return@flow
        }
        val ok =
            firestoreService.setDocument("${userChatsPath(uid)}/${chat.id}", chat.toFields(), token)
        if (ok) emit(Result.Success(Unit)) else emit(Result.Failure(ERROR_FIRESTORE_WRITE_FAILED))
    }

    override fun deleteChat(chat: Chat): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure(ERROR_NOT_AUTHENTICATED))
            return@flow
        }
        val deleted = firestoreService.deleteDocument("${userChatsPath(uid)}/${chat.id}", token)
        if (!deleted) {
            emit(Result.Failure(ERROR_FIRESTORE_DELETE_CHAT_FAILED))
            return@flow
        }
        val query = FirestoreStructuredQuery(
            structuredQuery = FirestoreQuery(
                from = listOf(FirestoreCollectionSelector(MESSAGES)),
                where = FirestoreFilter(
                    fieldFilter = FirestoreFieldFilter(
                        field = FirestoreFieldReference(FIELD_CHAT_ID),
                        op = FirestoreConstants.FILTER_OP_EQUAL,
                        value = FirestoreValue(integerValue = chat.id.toString()),
                    )
                )
            )
        )
        val msgDocs = firestoreService.runQuery("$USERS/$uid", query, token)
        msgDocs.forEach { doc ->
            doc.name?.substringAfterLast('/')?.let { id ->
                firestoreService.deleteDocument("${userMessagesPath(uid)}/$id", token)
            }
        }
        emit(Result.Success(Unit))
    }

    override fun insertMessage(message: Message): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure(ERROR_NOT_AUTHENTICATED))
            return@flow
        }
        val ok = firestoreService.setDocument(
            "${userMessagesPath(uid)}/${message.id}",
            message.toFields(),
            token
        )
        if (ok) emit(Result.Success(Unit)) else emit(Result.Failure(ERROR_FIRESTORE_WRITE_FAILED))
    }

    override fun deleteMessages(messageIds: List<String>): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure(ERROR_NOT_AUTHENTICATED))
            return@flow
        }
        messageIds.forEach { id ->
            firestoreService.deleteDocument("${userMessagesPath(uid)}/$id", token)
        }
        emit(Result.Success(Unit))
    }

    override fun deleteMessagesByChatId(chatId: Long): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure(ERROR_NOT_AUTHENTICATED))
            return@flow
        }
        val query = FirestoreStructuredQuery(
            structuredQuery = FirestoreQuery(
                from = listOf(FirestoreCollectionSelector(MESSAGES)),
                where = FirestoreFilter(
                    fieldFilter = FirestoreFieldFilter(
                        field = FirestoreFieldReference(FIELD_CHAT_ID),
                        op = FirestoreConstants.FILTER_OP_EQUAL,
                        value = FirestoreValue(integerValue = chatId.toString()),
                    )
                )
            )
        )
        val docs = firestoreService.runQuery("$USERS/$uid", query, token)
        docs.forEach { doc ->
            doc.name?.substringAfterLast('/')?.let { id ->
                firestoreService.deleteDocument("${userMessagesPath(uid)}/$id", token)
            }
        }
        emit(Result.Success(Unit))
    }

    override fun setReviewVoted(): Flow<Unit> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) return@flow
        firestoreService.setDocument(
            "$USERS/$uid/${FirestoreConstants.PATH_META_REVIEW}",
            mapOf(FIELD_VOTED to firestoreBool(true)),
            token
        )
        emit(Unit)
    }

    override fun getPrivacyPolicy(appLang: String): Flow<String> = flow {
        val doc = firestoreService.getDocument("$PRIVACY_POLICY/$appLang", "")
        val text = doc?.fields?.get(FIELD_TEXT)?.stringValue.orEmpty()
        emit(text)
    }

    override fun getRemoteSettings(): Flow<Result<Map<String, String?>>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure(ERROR_NOT_AUTHENTICATED))
            return@flow
        }
        val doc = firestoreService.getDocument(userSettingsPath(uid), token)
        val f = doc?.fields
        emit(
            Result.Success(
                mapOf(
                    FIELD_AI_MODEL to f?.get(FIELD_AI_MODEL)?.stringValue,
                    FIELD_API_KEY to f?.get(FIELD_API_KEY)?.stringValue,
                    FIELD_TEMPERATURE to f?.get(FIELD_TEMPERATURE)?.stringValue,
                    FIELD_GLOBAL_CONTEXT to f?.get(FIELD_GLOBAL_CONTEXT)?.stringValue,
                )
            )
        )
    }

    override fun setRemoteSettings(settings: Map<String, String?>): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure(ERROR_NOT_AUTHENTICATED))
            return@flow
        }
        val fields = settings.mapValues { (_, v) -> firestoreString(v) }
        val ok = firestoreService.setDocument(userSettingsPath(uid), fields, token)
        if (ok) emit(Result.Success(Unit)) else emit(Result.Failure(ERROR_FIRESTORE_WRITE_FAILED))
    }
}
