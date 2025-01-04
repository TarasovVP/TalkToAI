package com.vnteam.talktoai.domain.sealed_classes

import com.vnteam.talktoai.Res
import com.vnteam.talktoai.ic_tab_four
import com.vnteam.talktoai.ic_tab_one
import com.vnteam.talktoai.ic_tab_three
import com.vnteam.talktoai.ic_tab_two
import com.vnteam.talktoai.onboarding_intro
import com.vnteam.talktoai.presentation.ui.resources.StringResources
import org.jetbrains.compose.resources.DrawableResource

sealed class OnboardingPage(
    val page: Int,
    val mainImage: DrawableResource,
    val tabImage: DrawableResource
) {
    data object OnboardingPageIntro :
        OnboardingPage(0, Res.drawable.onboarding_intro, Res.drawable.ic_tab_one)

    data object OnboardingPageFilterConditions :
        OnboardingPage(1, Res.drawable.onboarding_intro, Res.drawable.ic_tab_two)

    data object OnboardingPageInfo :
        OnboardingPage(2, Res.drawable.onboarding_intro, Res.drawable.ic_tab_three)

    data object OnboardingPagePermissions :
        OnboardingPage(3, Res.drawable.onboarding_intro, Res.drawable.ic_tab_four)

    companion object {
        fun OnboardingPage.getOnboardingPageDescription(getStringResources: StringResources): String {
            return when (page) {
                1 -> getStringResources.ONBOARDING_FILTER_CONDITIONS
                2 -> getStringResources.ONBOARDING_INFO
                3 -> getStringResources.ONBOARDING_PERMISSIONS
                else -> getStringResources.ONBOARDING_INTRO
            }
        }
        fun getOnboardingPage(page: Int): OnboardingPage {
            return when (page) {
                1 -> OnboardingPageFilterConditions
                2 -> OnboardingPageInfo
                3 -> OnboardingPagePermissions
                else -> OnboardingPageIntro
            }
        }
    }
}
