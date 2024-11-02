package com.vnteam.talktoai.data.mapperimpls

import com.vnteam.talktoai.ChatDB
import com.vnteam.talktoai.domain.mappers.ChatDBMapper
import com.vnteam.talktoai.domain.models.Chat

class ChatDBMapperImpl : ChatDBMapper {

    override fun mapToImplModel(from: Chat): ChatDB {
        return ChatDB(
            id = from.id,
            name = from.name,
            updated = from.updated,
            listOrder = from.listOrder
        )
    }

    override fun mapFromImplModel(to: ChatDB): Chat {
        return Chat(
            id = to.id,
            name = to.name,
            updated = to.updated,
            listOrder = to.listOrder
        )
    }

    override fun mapToImplModelList(fromList: List<Chat>): List<ChatDB> {
        return fromList.map { mapToImplModel(it) }
    }

    override fun mapFromImplModelList(toList: List<ChatDB>): List<Chat> {
        return toList.map { mapFromImplModel(it) }
    }
}