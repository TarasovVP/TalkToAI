package com.vn.talktoai.presentation.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.vn.talktoai.CommonExtensions.safeSingleObserve
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.models.Message
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        viewModel.completionLiveData.safeSingleObserve(viewLifecycleOwner) {
            setContent {
                MessageList(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiRequest = ApiRequest(model = "gpt-3.5-turbo", temperature = 0.7f, messages = listOf(Message(role = "user", content = "Who are you?")))
        viewModel.getCompletions(apiRequest)
    }
}