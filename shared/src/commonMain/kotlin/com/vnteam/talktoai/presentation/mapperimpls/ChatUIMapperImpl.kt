package com.vnteam.talktoai.presentation.mapperimpls

import com.vnteam.talktoai.CommonExtensions.orZero
import com.vnteam.talktoai.Constants.DEFAULT_CHAT_ID
import com.vnteam.talktoai.domain.mappers.ChatUIMapper
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.presentation.uimodels.ChatUI

class ChatUIMapperImpl : ChatUIMapper {

    override fun mapToImplModel(from: Chat): ChatUI {
        return ChatUI(
            id = from.id ?: DEFAULT_CHAT_ID,
            name = from.name.orEmpty(),
            updated = from.updated.orZero(),
            listOrder = from.listOrder.orZero()
        )
    }

    override fun mapFromImplModel(to: ChatUI): Chat {
        return Chat(
            id = to.id,
            name = to.name,
            updated = to.updated,
            listOrder = to.listOrder
        )
    }

    override fun mapToImplModelList(fromList: List<Chat>): List<ChatUI> {
        return fromList.map { mapToImplModel(it) }
    }

    override fun mapFromImplModelList(toList: List<ChatUI>): List<Chat> {
        return toList.map { mapFromImplModel(it) }
    }
}