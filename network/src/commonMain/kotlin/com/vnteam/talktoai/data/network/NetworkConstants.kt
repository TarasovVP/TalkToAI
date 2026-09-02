package com.vnteam.talktoai.data.network

object NetworkConstants {
    const val BEARER_PREFIX = "Bearer "
    const val OPENAI_AUTHORIZATION_HEADER = "Authorization"
    const val OPENAI_ORGANIZATION_HEADER = "OpenAI-Organization"
    const val OPENAI_PROJECT_HEADER = "OpenAI-Project"

    // Anthropic
    const val ANTHROPIC_API_KEY_HEADER = "x-api-key"
    const val ANTHROPIC_VERSION_HEADER = "anthropic-version"
    const val ANTHROPIC_VERSION = "2023-06-01"

    // JSON error response field names
    const val ERROR_KEY = "error"
    const val MESSAGE_KEY = "message"
    const val TYPE_KEY = "type"

    // Error type values
    const val ERROR_TYPE_NOT_FOUND = "not_found_error"
    const val ERROR_TYPE_INVALID_REQUEST = "invalid_request_error"

    // Error message keywords
    const val MODEL_KEYWORD = "model"
    const val TEMPERATURE_KEYWORD = "temperature"
}
