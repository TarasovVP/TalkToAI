package com.vnteam.talktoai

object SettingsConstants {

    // AI Temperature
    const val AI_TEMPERATURE_DEFAULT = 0.7f
    const val AI_TEMPERATURE_MIN = 0f
    const val AI_TEMPERATURE_MAX = 2f

    // Default model IDs (BALANCED tier for each provider)
    const val OPENAI_AI_MODEL_DEFAULT = "gpt-5.6-terra"
    const val ANTHROPIC_MODEL_DEFAULT = "claude-sonnet-5"

    // AI Providers
    const val AI_PROVIDER_OPENAI = "OPENAI"
    const val AI_PROVIDER_ANTHROPIC = "ANTHROPIC"
}
