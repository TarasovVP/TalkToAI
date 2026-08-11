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
}
