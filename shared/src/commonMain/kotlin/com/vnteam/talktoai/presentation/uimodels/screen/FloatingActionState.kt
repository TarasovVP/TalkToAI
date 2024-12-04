package com.vnteam.talktoai.presentation.uimodels.screen

data class FloatingActionState(
    var floatingActionButtonVisible: Boolean = false,
    var floatingActionButtonTitle: String = "",
    var floatingActionButtonAction: () -> Unit = {}
)
