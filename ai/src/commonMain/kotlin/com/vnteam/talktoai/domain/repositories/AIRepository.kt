package com.vnteam.talktoai.domain.repositories

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.request.Message
import com.vnteam.talktoai.domain.enums.AiProviderType
import kotlinx.coroutines.flow.Flow

interface AIRepository {
    fun sendRequest(
        model: String,
        temperature: Float,
        messages: List<Message>,
        apiKey: String? = null,
        providerType: AiProviderType = AiProviderType.OPENAI,
    ): Flow<Result<AiTextResponse>>
}
