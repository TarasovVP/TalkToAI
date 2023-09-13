package com.vnstudio.talktoai.domain.mappers

import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.presentation.ui_models.MessageUIModel

interface MessageUIMapper : BaseMapper<Message, MessageUIModel>