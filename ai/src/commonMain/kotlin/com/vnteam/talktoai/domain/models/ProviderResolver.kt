package com.vnteam.talktoai.domain.models

import com.vnteam.talktoai.domain.enums.AiProviderType

fun resolveEffectiveProvider(chatAiProvider: String?, globalProvider: AiProviderType): AiProviderType =
    chatAiProvider?.let { runCatching { AiProviderType.valueOf(it) }.getOrNull() } ?: globalProvider
