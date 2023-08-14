package com.vnstudio.talktoai.presentation.main

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
import com.vnstudio.talktoai.CommonExtensions.moveElementToTop
import com.vnstudio.talktoai.CommonExtensions.safeSingleObserve
import com.vnstudio.talktoai.MainNavigationDirections
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.databinding.ContentMainBinding
import com.vnstudio.talktoai.ui.theme.TalkToAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(
            ComposeView(this).apply {
                setContent {
                    ProvideWindowInsets(consumeWindowInsets = false) {
                        TalkToAITheme {
                            MainScreen (mainViewModel,
                                onChatClicked = { chatId ->
                                    findNavController().navigate(MainNavigationDirections.startChatFragment(chatId))
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