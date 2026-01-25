package com.vnteam.talktoai.presentation.uimodels.screen

data class AppBarState(
    var topAppBarVisible: Boolean = true,
    var appBarTitle: String = "",
    var topAppBarActionVisible: Boolean = false,
    var topAppBarAction: () -> Unit = {},
)
