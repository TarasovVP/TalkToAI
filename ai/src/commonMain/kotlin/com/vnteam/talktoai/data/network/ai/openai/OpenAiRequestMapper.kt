package com.vnteam.talktoai.data.network.ai.openai

import com.vnteam.talktoai.data.network.ai.openai.request.ApiRequest
import com.vnteam.talktoai.data.network.ai.openai.request.OpenAiContentBlock
import com.vnteam.talktoai.data.network.ai.openai.request.OpenAiImageBlock
import com.vnteam.talktoai.data.network.ai.openai.request.OpenAiImageUrl
import com.vnteam.talktoai.data.network.ai.openai.request.OpenAiMessage
import com.vnteam.talktoai.data.network.ai.openai.request.OpenAiTextBlock
import com.vnteam.talktoai.data.network.ai.request.Message
import com.vnteam.talktoai.domain.models.MessageContent

fun List<Message>.toOpenAiRequest(model: String, temperature: Float? = null): ApiRequest {
    val apiMessages = map { msg ->
        OpenAiMessage(
            role = msg.role,
            content = msg.content.map { it.toOpenAiBlock() },
        )
    }
    return ApiRequest(model = model, messages = apiMessages, temperature = temperature)
}

private fun MessageContent.toOpenAiBlock(): OpenAiContentBlock = when (this) {
    is MessageContent.Text -> OpenAiTextBlock(text = text)
    is MessageContent.Image -> OpenAiImageBlock(
        imageUrl = OpenAiImageUrl(url = "data:$mimeType;base64,$base64Data"),
    )
}
