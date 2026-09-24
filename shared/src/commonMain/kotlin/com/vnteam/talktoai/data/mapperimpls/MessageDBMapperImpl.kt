package com.vnteam.talktoai.data.mapperimpls

import com.vnteam.talktoai.CommonExtensions.orZero
import com.vnteam.talktoai.MessageDB
import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.domain.mappers.MessageDBMapper
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.MessageContent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

class MessageDBMapperImpl : MessageDBMapper {

    override fun mapToImplModel(from: Message): MessageDB {
        val textFallback = from.message
        val textOnly = from.content.filterIsInstance<MessageContent.Text>()
        val contentJson = runCatching { json.encodeToString<List<MessageContent>>(textOnly) }.getOrNull()
        return MessageDB(
            from.id.orZero(),
            from.chatId,
            from.author,
            textFallback,
            from.updatedAt,
            from.status?.name,
            from.errorMessage,
            if (from.truncated) 1 else 0,
            contentJson,
            if (from.isComplete) 1 else 0,
            from.inputTokens?.toLong(),
            from.outputTokens?.toLong(),
            from.cacheReadTokens?.toLong(),
            from.cacheWriteTokens?.toLong(),
        )
    }

    override fun mapFromImplModel(to: MessageDB): Message {
        val content = to.contentJson
            ?.let { runCatching { json.decodeFromString<List<MessageContent>>(it) }.getOrNull() }
            ?: listOf(MessageContent.Text(to.message.orEmpty()))
        return Message(
            to.id,
            to.chatId,
            to.author,
            content,
            to.updatedAt,
            to.status?.let { runCatching { MessageStatus.valueOf(it) }.getOrNull() },
            to.errorMessage,
            to.truncated == 1L,
            to.isComplete == null || to.isComplete == 1L,
            to.inputTokens?.toInt(),
            to.outputTokens?.toInt(),
            to.cacheReadTokens?.toInt(),
            to.cacheWriteTokens?.toInt(),
        )
    }

    override fun mapToImplModelList(fromList: List<Message>): List<MessageDB> =
        fromList.map { mapToImplModel(it) }

    override fun mapFromImplModelList(toList: List<MessageDB>): List<Message> =
        toList.map { mapFromImplModel(it) }
}
