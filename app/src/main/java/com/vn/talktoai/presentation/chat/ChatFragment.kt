package com.vn.talktoai.presentation.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.viewModels
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.gson.Gson
import com.vn.talktoai.databinding.ContentMainBinding
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.models.Message

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
        setContent {
            MaterialTheme {
                ProvideWindowInsets(consumeWindowInsets = false) {
                    AndroidViewBinding(ContentMainBinding::inflate)
                }
            }
        }
        val apiRequest = ApiRequest(model = "gpt-3.5-turbo", temperature = 0.7f, messages = listOf(Message(role = "user", content = "Who are you?")))
        viewModel.getCompletions(apiRequest)
        viewModel.completionLiveData.observe(viewLifecycleOwner) { apiResponse ->
            setContent {
                CustomTextView(Gson().toJson(apiResponse))
            }
        }
    }

}