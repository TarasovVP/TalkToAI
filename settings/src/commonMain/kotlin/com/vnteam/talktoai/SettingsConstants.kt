package com.vnteam.talktoai

object SettingsConstants {

    // AI Models
    const val AI_MODEL_DEFAULT = "gpt-3.5-turbo"
    val AI_MODELS = listOf("gpt-4o", "gpt-4o-mini", "gpt-4-turbo", "gpt-4", "gpt-3.5-turbo")

    // AI Temperature
    const val AI_TEMPERATURE_DEFAULT = 0.7f
    const val AI_TEMPERATURE_MIN = 0f
    const val AI_TEMPERATURE_MAX = 2f
}
