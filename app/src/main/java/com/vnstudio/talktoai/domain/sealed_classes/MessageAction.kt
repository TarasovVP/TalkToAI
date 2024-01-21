package com.vnstudio.talktoai.domain.sealed_classes

import com.vnstudio.talktoai.infrastructure.Constants.MESSAGE_ACTION_CANCEL
import com.vnstudio.talktoai.infrastructure.Constants.MESSAGE_ACTION_COPY
import com.vnstudio.talktoai.infrastructure.Constants.MESSAGE_ACTION_DELETE
import com.vnstudio.talktoai.infrastructure.Constants.MESSAGE_ACTION_SHARE
import com.vnstudio.talktoai.infrastructure.Constants.MESSAGE_ACTION_TRANSFER

sealed class MessageAction {
    data class Cancel(val value: String = MESSAGE_ACTION_CANCEL) : MessageAction()
    data class Copy(val value: String = MESSAGE_ACTION_COPY) : MessageAction()
    data class Delete(val value: String = MESSAGE_ACTION_DELETE) : MessageAction()
    data class Transfer(val value: String = MESSAGE_ACTION_TRANSFER) : MessageAction()
    data class Share(val value: String = MESSAGE_ACTION_SHARE) : MessageAction()
}
