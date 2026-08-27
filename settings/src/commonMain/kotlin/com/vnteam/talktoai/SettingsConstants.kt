package com.vnteam.talktoai

object SettingsConstants {

    // AI Models
    const val AI_MODEL_GPT_4O = "gpt-4o"
    const val AI_MODEL_GPT_4O_MINI = "gpt-4o-mini"
    const val AI_MODEL_GPT_4_TURBO = "gpt-4-turbo"
    const val AI_MODEL_GPT_4 = "gpt-4"
    const val AI_MODEL_DEFAULT = "gpt-3.5-turbo"
    val AI_MODELS = listOf(AI_MODEL_GPT_4O, AI_MODEL_GPT_4O_MINI, AI_MODEL_GPT_4_TURBO, AI_MODEL_GPT_4, AI_MODEL_DEFAULT)

    // AI Temperature
    const val AI_TEMPERATURE_DEFAULT = 0.7f
    const val AI_TEMPERATURE_MIN = 0f
    const val AI_TEMPERATURE_MAX = 2f

    // AI Providers
    const val AI_PROVIDER_OPENAI = "OPENAI"
    const val AI_PROVIDER_ANTHROPIC = "ANTHROPIC"

    // Anthropic Models
    const val ANTHROPIC_MODEL_OPUS_5 = "claude-opus-5"
    const val ANTHROPIC_MODEL_SONNET_5 = "claude-sonnet-5"
    const val ANTHROPIC_MODEL_HAIKU_4_5 = "claude-haiku-4-5"
    const val ANTHROPIC_MODEL_DEFAULT = ANTHROPIC_MODEL_SONNET_5
    val ANTHROPIC_MODELS = listOf(ANTHROPIC_MODEL_OPUS_5, ANTHROPIC_MODEL_SONNET_5, ANTHROPIC_MODEL_HAIKU_4_5)
}
