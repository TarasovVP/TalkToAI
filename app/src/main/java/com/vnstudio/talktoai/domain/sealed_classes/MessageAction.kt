package com.vnstudio.talktoai.domain.sealed_classes

sealed class MessageAction {
    object Cancel : MessageAction()
    object Copy : MessageAction()
    object Delete : MessageAction()
    object Transfer : MessageAction()
    object Share : MessageAction()
}
