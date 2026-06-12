package com.vnteam.talktoai.presentation

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandlerWrapper(enabled: Boolean = true, onBack: () -> Unit)
