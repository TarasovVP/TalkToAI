package com.vnteam.talktoai.presentation.states.screen

data class AppBarState(var topAppBarVisible: Boolean = true,
                       var appBarTitle: String = "",
                       var topAppBarActionVisible: Boolean = false,
                       var topAppBarAction: () -> Unit = {})
