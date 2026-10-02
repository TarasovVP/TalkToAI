package com.vnteam.talktoai.presentation.ui.resources

import com.vnteam.talktoai.domain.enums.ModelTier

fun StringResources.labelFor(tier: ModelTier): String = when (tier) {
    ModelTier.FAST -> SETTINGS_CHAT_TIER_FAST
    ModelTier.BALANCED -> SETTINGS_CHAT_TIER_BALANCED
    ModelTier.POWERFUL -> SETTINGS_CHAT_TIER_POWERFUL
}
