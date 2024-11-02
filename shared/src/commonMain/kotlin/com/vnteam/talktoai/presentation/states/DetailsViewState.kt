package com.vnteam.talktoai.presentation.states

import com.vnteam.talktoai.presentation.uimodels.DemoObjectUI


data class DetailsViewState(val demoObjectUI: DemoObjectUI? = null,
                            var successResult: Boolean = false
)
