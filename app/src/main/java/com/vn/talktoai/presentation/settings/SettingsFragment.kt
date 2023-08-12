package com.vn.talktoai.presentation.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.vn.talktoai.CommonExtensions.safeSingleObserve
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setContent {
            SettingsContent {
                viewModel.changeSettings()
            }
        }
        viewModel.settingsLiveData.safeSingleObserve(viewLifecycleOwner) {
            Log.e(
                "apiTAG",
                "SettingsFragment onCreateView settingsLiveData safeSingleObserve"
            )
        }
    }
}