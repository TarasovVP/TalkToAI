package com.vn.talktoai.presentation.chat

import com.vn.talktoai.domain.repositories.AIRepository
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.usecases.ChatUseCase
import javax.inject.Inject

class ChatUseCaseImpl @Inject constructor(private val aiRepository: AIRepository) : ChatUseCase {

    override suspend fun getCompletions(apiRequest: ApiRequest) = aiRepository.getCompletions(apiRequest)
}