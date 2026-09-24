package com.vnteam.talktoai.presentation.mapperimpls

import com.vnteam.talktoai.CommonExtensions.orZero
import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.domain.mappers.MessageUIMapper
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.MessageContent
import com.vnteam.talktoai.presentation.uimodels.MessageUI

class MessageUIMapperImpl : MessageUIMapper {

    override fun mapToImplModel(from: Message): MessageUI {
        return MessageUI(
            from.id.orZero(),
            from.chatId.orZero(),
            from.author.orEmpty(),
            from.message,
            from.updatedAt.orZero(),
            from.status?.name?.let { MessageStatus.valueOf(it) } ?: MessageStatus.REQUESTING,
            from.errorMessage.orEmpty(),
            from.truncated,
            from.isComplete,
            from.inputTokens,
            from.outputTokens,
            from.cacheReadTokens,
            from.cacheWriteTokens,
        )
    }

    override fun mapFromImplModel(to: MessageUI): Message {
        val imageBlock = to.attachedImage
        val content = buildList {
            if (to.message.isNotEmpty()) add(MessageContent.Text(to.message))
            if (imageBlock != null) add(imageBlock)
        }
        return Message(
            to.id,
            to.chatId,
            to.author,
            content,
            to.updatedAt,
            to.status,
            to.errorMessage,
            to.isTruncated,
            to.isComplete,
            to.inputTokens,
            to.outputTokens,
            to.cacheReadTokens,
            to.cacheWriteTokens,
        )
    }

    override fun mapToImplModelList(fromList: List<Message>): List<MessageUI> =
        fromList.map { mapToImplModel(it) }

    override fun mapFromImplModelList(toList: List<MessageUI>): List<Message> =
        toList.map { mapFromImplModel(it) }
}
