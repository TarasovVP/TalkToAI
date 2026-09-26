package com.vnteam.talktoai.domain.aimodels

import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.domain.models.Message

fun lastAssistantInputTokens(messages: List<Message>): Int? =
    messages.lastOrNull { it.author != Constants.MESSAGE_ROLE_ME && it.inputTokens != null }?.inputTokens

fun formatTokenCount(value: Int): String =
    value.toString().reversed().chunked(3).joinToString(" ").reversed()

fun formatContextUsage(usedTokens: Int, contextWindow: Int): String {
    val used = formatTokenCount(usedTokens)
    if (contextWindow <= 0) return used
    val percent = usedTokens.toLong() * 100 / contextWindow
    val percentText = if (percent == 0L && usedTokens > 0) "<1%" else "$percent%"
    return "$used / ${formatTokenCount(contextWindow)} ($percentText)"
}
