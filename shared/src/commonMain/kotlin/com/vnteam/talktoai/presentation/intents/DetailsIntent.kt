package com.vnteam.talktoai.presentation.intents

sealed class DetailsIntent {
    data class LoadDemoObject(val demoObjectId: String, val isUpdated: Boolean = false) : DetailsIntent()
}