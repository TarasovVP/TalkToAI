package com.vnteam.talktoai.data.network.firestore

object FirestoreConstants {
    const val NULL_VALUE = "NULL_VALUE"
    const val FILTER_OP_EQUAL = "EQUAL"

    // Chat fields
    const val FIELD_ID = "id"
    const val FIELD_NAME = "name"
    const val FIELD_UPDATED = "updated"
    const val FIELD_LIST_ORDER = "listOrder"
    const val FIELD_AI_MODEL = "aiModel"
    const val FIELD_TEMPERATURE = "temperature"
    const val FIELD_CONTEXT = "context"

    // Message fields
    const val FIELD_CHAT_ID = "chatId"
    const val FIELD_AUTHOR = "author"
    const val FIELD_MESSAGE = "message"
    const val FIELD_UPDATED_AT = "updatedAt"
    const val FIELD_STATUS = "status"
    const val FIELD_ERROR_MESSAGE = "errorMessage"
    const val FIELD_TRUNCATED = "truncated"

    // Settings fields
    const val FIELD_AI_PROVIDER = "aiProvider"
    const val FIELD_API_KEY = "apiKey"
    const val FIELD_GLOBAL_CONTEXT = "globalContext"
    const val FIELD_TEXT = "text"
    const val FIELD_VOTED = "voted"

    // Paths
    const val PATH_GLOBAL = "global"
    const val PATH_META_REVIEW = "meta/review"
}
