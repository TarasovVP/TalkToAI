package com.vnteam.talktoai.presentation.mapperimpls

import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.presentation.mappers.DemoObjectUIMapper
import com.vnteam.talktoai.presentation.mappers.OwnerUIMapper
import com.vnteam.talktoai.presentation.uimodels.DemoObjectUI
import com.vnteam.talktoai.presentation.uimodels.OwnerUI

class DemoObjectUIMapperImpl(private val owner: OwnerUIMapper) : DemoObjectUIMapper {

    override fun mapToImplModel(from: Chat): DemoObjectUI {
        return DemoObjectUI(
            demoObjectId = from.demoObjectId,
            name = from.name,
            owner = owner.mapToImplModel(from.owner ?: Owner()),
            htmlUrl = from.htmlUrl,
            description = from.description
        )
    }

    override fun mapFromImplModel(to: DemoObjectUI): Chat {
        return Chat(
            demoObjectId = to.demoObjectId,
            name = to.name,
            owner = owner.mapFromImplModel(to.owner ?: OwnerUI()),
            htmlUrl = to.htmlUrl,
            description = to.description
        )
    }

    override fun mapToImplModelList(fromList: List<Chat>): List<DemoObjectUI> {
        return fromList.map { mapToImplModel(it) }
    }

    override fun mapFromImplModelList(toList: List<DemoObjectUI>): List<Chat> {
        return toList.map { mapFromImplModel(it) }
    }
}