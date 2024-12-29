package presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.CommonExtensions.isNotTrue
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.ic_arrow_back
import com.vnteam.talktoai.ic_edit
import com.vnteam.talktoai.ic_navigation
import com.vnteam.talktoai.presentation.ui.NavigationScreen.Companion.settingsScreenNameByRoute
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral50
import com.vnteam.talktoai.presentation.ui.theme.Primary100
import com.vnteam.talktoai.presentation.ui.theme.Primary700
import com.vnteam.talktoai.presentation.ui.theme.Primary900
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppTopBar(screenState: ScreenState?, onNavigationIconClick: () -> Unit) {
    when {
        screenState?.isLoggedInUser.isNotTrue() -> Unit
        screenState?.isMessageActionModeState?.value.isTrue() -> DeleteModeTopBar(
            LocalStringResources.current.MESSAGE_ACTION_SELECTED
        )

        screenState?.isSecondaryScreen.isTrue() -> SecondaryTopBar(
            settingsScreenNameByRoute(
                screenState?.currentScreenRoute,
                LocalStringResources.current
            ),
            onNavigationIconClick
        )

        else -> PrimaryTopBar(
            title = if (screenState?.isChatScreen.isTrue()) screenState?.currentChat?.name
                ?: LocalStringResources.current.APP_NAME else settingsScreenNameByRoute(
                screenState?.currentScreenRoute,
                LocalStringResources.current
            ),
            onNavigationIconClick,
            isActionVisible = screenState?.isChatScreen.isTrue() && screenState?.currentChat != null
        ) {
            //showEditChatDialog.value = true
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTopBar(
    title: String,
    onNavigationIconClick: () -> Unit,
    isActionVisible: Boolean,
    onActionIconClick: () -> Unit,
) {
    TopAppBar(title = { Text(title) }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Primary900, titleContentColor = Neutral50
    ), navigationIcon = {
        IconButton(onClick = onNavigationIconClick) {
            Icon(
                painter = painterResource(Res.drawable.ic_navigation),
                contentDescription = LocalStringResources.current.NAVIGATION_ICON,
                tint = Primary100
            )
        }
    }, actions = {
        if (isActionVisible) {
            IconButton(
                onClick = onActionIconClick
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_edit),
                    contentDescription = LocalStringResources.current.CHAT_EDIT_BUTTON,
                    tint = Primary100
                )
            }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTopBar(title: String, onNavigationIconClick: () -> Unit) {
    TopAppBar(title = { Text(title) }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Primary900, titleContentColor = Neutral50
    ), navigationIcon = {
        IconButton(onClick = onNavigationIconClick) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = LocalStringResources.current.NAVIGATION_ICON
            )
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteModeTopBar(title: String) {
    TopAppBar(
        title = { Text(title) }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Primary900, titleContentColor = Neutral50
        )
    )
}

@Composable
fun AppSnackBar(screenState: ScreenState?, scope: CoroutineScope) {
    val snackBarHostState = remember { SnackbarHostState() }
    screenState?.appMessageState?.let { infoMessage ->
        scope.launch {
            snackBarHostState.showSnackbar(
                message = infoMessage.messageText,
                //actionLabel = infoMessage.isMessageError,
                duration = SnackbarDuration.Short
            )
        }
    }
    SnackbarHost(hostState = snackBarHostState, snackbar = { data ->
        Box {
            Snackbar(
                modifier = Modifier.padding(8.dp),
                containerColor = if (data.visuals.actionLabel == Constants.ERROR_MESSAGE) Color.Red else Primary700
            ) {
                Text(data.visuals.message)
            }
            Spacer(modifier = Modifier.fillMaxSize())
        }
    })
}