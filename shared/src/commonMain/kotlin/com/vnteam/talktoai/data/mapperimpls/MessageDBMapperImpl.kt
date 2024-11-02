package com.vnteam.talktoai.data.mapperimpls

import com.vnteam.talktoai.CommonExtensions.orZero
import com.vnteam.talktoai.MessageDB
import com.vnteam.talktoai.domain.MessageStatus
import com.vnteam.talktoai.domain.mappers.MessageDBMapper
import com.vnteam.talktoai.domain.models.Message

class MessageDBMapperImpl : MessageDBMapper {

    override fun mapToImplModel(from: Message): MessageDB {
        return MessageDB(
            from.id.orZero(),
            from.chatId,
            from.author,
            from.message,
            from.updatedAt,
            from.status?.name,
            from.errorMessage,
            if (from.truncated) 1 else 0
        )
    }

    override fun mapFromImplModel(to: MessageDB): Message {
        return Message(
            to.id,
            to.chatId,
            to.author,
            to.message,
            to.updatedAt,
            MessageStatus.valueOf(to.status.orEmpty()),
            to.errorMessage,
            to.truncated == 1L
        )
    }

    override fun mapToImplModelList(fromList: List<Message>): List<MessageDB> {
        return fromList.map { mapToImplModel(it) }
    }

    override fun mapFromImplModelList(toList: List<MessageDB>): List<Message> {
        return toList.map { mapFromImplModel(it) }
    }
}