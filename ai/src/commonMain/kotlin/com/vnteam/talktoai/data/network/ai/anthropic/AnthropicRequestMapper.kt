package com.vnteam.talktoai.data.network.ai.anthropic

import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicContentBlock
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicMessage
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicRequest
import com.vnteam.talktoai.data.network.ai.openai.request.MessageApi

private const val ROLE_SYSTEM = "system"
private const val ROLE_USER = "user"
private const val ROLE_ASSISTANT = "assistant"

fun List<MessageApi>.toAnthropicRequest(
    model: String,
    temperature: Float,
): AnthropicRequest {
    val systemContent = filter { it.role == ROLE_SYSTEM }
        .mapNotNull { it.content }
        .joinToString("\n")
        .takeIf { it.isNotBlank() }

    val anthropicMessages = filter { it.role == ROLE_USER || it.role == ROLE_ASSISTANT }
        .map { msg ->
            AnthropicMessage(
                role = msg.role ?: ROLE_USER,
                content = listOf(AnthropicContentBlock(text = msg.content.orEmpty())),
            )
        }

    return AnthropicRequest(
        model = model,
        temperature = temperature,
        system = systemContent,
        messages = anthropicMessages,
    )
}
