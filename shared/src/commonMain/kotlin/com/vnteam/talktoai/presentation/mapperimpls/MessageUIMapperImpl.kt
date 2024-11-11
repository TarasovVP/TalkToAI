package com.vnteam.talktoai.presentation.mapperimpls

import com.vnteam.talktoai.CommonExtensions.orZero
import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.domain.mappers.MessageUIMapper
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.presentation.uimodels.MessageUI

class MessageUIMapperImpl : MessageUIMapper {

    override fun mapToImplModel(from: Message): MessageUI {
        return MessageUI(
            from.id.orZero(),
            from.chatId.orZero(),
            from.author.orEmpty(),
            from.message.orEmpty(),
            from.updatedAt.orZero(),
            from.status?.name?.let { MessageStatus.valueOf(it) } ?: MessageStatus.REQUESTING,
            from.errorMessage.orEmpty(),
            from.truncated
        )
    }

    override fun mapFromImplModel(to: MessageUI): Message {
        return Message(
            to.id,
            to.chatId,
            to.author,
            to.message,
            to.updatedAt,
            MessageStatus.REQUESTING,
            to.errorMessage,
            to.isTruncated
        )
    }

    override fun mapToImplModelList(fromList: List<Message>): List<MessageUI> {
        return fromList.map { mapToImplModel(it) }
    }

    override fun mapFromImplModelList(toList: List<MessageUI>): List<Message> {
        return toList.map { mapFromImplModel(it) }
    }
}