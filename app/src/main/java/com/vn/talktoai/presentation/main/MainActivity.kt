package com.vn.talktoai.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.accompanist.insets.ProvideWindowInsets
import com.vn.talktoai.CommonExtensions.moveElementToTop
import com.vn.talktoai.CommonExtensions.safeSingleObserve
import com.vn.talktoai.MainNavigationDirections
import com.vn.talktoai.R
import com.vn.talktoai.data.database.db_entities.Chat
import com.vn.talktoai.databinding.ContentMainBinding
import com.vn.talktoai.ui.theme.TalkToAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chatList = mutableListOf<Chat>().toMutableStateList()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(
            ComposeView(this).apply {
                setContent {
                    ProvideWindowInsets(consumeWindowInsets = false) {
                        TalkToAITheme {
                            MainScreen (chatList,
                                onAddChatClicked = {
                                    mainViewModel.insertChat(Chat(name = "New Chat"))
                                },
                                onDeleteChatClicked = { chat ->
                                    mainViewModel.deleteChat(chat)
                                },
                                onEditChatClicked = { chat ->
                                    mainViewModel.updateChat(chat)
                                },
                                onChatClicked = { chat ->
                                    chatList.moveElementToTop(chat)
                                    findNavController().navigate(MainNavigationDirections.startChatFragment(chat.chatId))
                                },
                                    onSettingsClicked = {
                                        findNavController().navigate(R.id.settingsFragment)
                                }, content =  {
                                    AndroidViewBinding(ContentMainBinding::inflate)
                                })
                        }
                    }
                }
            }
        )
        mainViewModel.getChats()
        mainViewModel.chatsLiveData.safeSingleObserve(this) { chats ->
            chatList.clear()
            chatList.addAll(chats)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController().navigateUp() || super.onSupportNavigateUp()
    }

    private fun findNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }
}