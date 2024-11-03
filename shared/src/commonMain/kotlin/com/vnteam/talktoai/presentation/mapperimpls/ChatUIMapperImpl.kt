package com.vnteam.talktoai.presentation.mapperimpls

import com.vnteam.talktoai.domain.mappers.ChatUIMapper
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.presentation.uimodels.ChatUI

class ChatUIMapperImpl : ChatUIMapper {

    override fun mapToImplModel(from: Chat): ChatUI {
        return ChatUI(
            id = from.id,
            name = from.name,
            updated = from.updated,
            listOrder = from.listOrder
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