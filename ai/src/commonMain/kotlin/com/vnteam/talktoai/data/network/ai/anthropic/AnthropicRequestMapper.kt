package com.vnteam.talktoai.data.network.ai.anthropic

import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicContentBlock
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicImageBlock
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicImageSource
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicMessage
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicRequest
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicTextBlock
import com.vnteam.talktoai.data.network.ai.request.Message
import com.vnteam.talktoai.domain.models.MessageContent

private const val ROLE_SYSTEM = "system"
private const val ROLE_USER = "user"
private const val ROLE_ASSISTANT = "assistant"

fun List<Message>.toAnthropicRequest(
    model: String,
    temperature: Float? = null,
): AnthropicRequest {
    val systemContent = filter { it.role == ROLE_SYSTEM }
        .flatMap { it.content.filterIsInstance<MessageContent.Text>() }
        .joinToString("\n") { it.text }
        .takeIf { it.isNotBlank() }

    val anthropicMessages = filter { it.role == ROLE_USER || it.role == ROLE_ASSISTANT }
        .map { msg ->
            AnthropicMessage(
                role = msg.role ?: ROLE_USER,
                content = msg.content.mapNotNull { it.toAnthropicBlock() },
            )
        }

    return AnthropicRequest(
        model = model,
        system = systemContent,
        temperature = temperature,
        messages = anthropicMessages,
    )
}

private fun MessageContent.toAnthropicBlock(): AnthropicContentBlock? = when (this) {
    is MessageContent.Text -> AnthropicTextBlock(text = text)
    is MessageContent.Image -> AnthropicImageBlock(
        source = AnthropicImageSource(
            mediaType = mimeType,
            data = base64Data,
        )
    )
}
