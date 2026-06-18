package com.vnteam.talktoai.presentation.ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun ChatSheetWrapper(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
)
