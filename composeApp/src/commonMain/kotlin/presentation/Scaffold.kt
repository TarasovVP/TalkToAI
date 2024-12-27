package presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.avatar_ai
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.ic_arrow_back
import com.vnteam.talktoai.ic_chat
import com.vnteam.talktoai.ic_edit
import com.vnteam.talktoai.ic_navigation
import com.vnteam.talktoai.ic_settings
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral50
import com.vnteam.talktoai.presentation.ui.theme.Primary100
import com.vnteam.talktoai.presentation.ui.theme.Primary700
import com.vnteam.talktoai.presentation.ui.theme.Primary900
import org.jetbrains.compose.resources.painterResource
import presentation.NavigationScreen.Companion.isSettingsScreen
import presentation.NavigationScreen.Companion.settingsScreenNameByRoute

@Composable
fun AppTopBar(
    currentNavRoute: String,
    onNavigationIconClick: () -> Unit,
    showEditChatDialog: MutableState<Boolean>,
    isMessageActionModeState: MutableState<Boolean?>,
    currentChatState: MutableState<Chat?>,
    chatsState: MutableState<List<Chat>?>
) {
    when {
        isMessageActionModeState.value.isTrue() -> DeleteModeTopBar(LocalStringResources.current.MESSAGE_ACTION_SELECTED)
        currentNavRoute == NavigationScreen.SettingsSignUpScreen().route -> SecondaryTopBar(
            settingsScreenNameByRoute(
                currentNavRoute, LocalStringResources.current
            )
        ) {
            onNavigationIconClick()
        }

        isSettingsScreen(currentNavRoute) || currentNavRoute == NavigationScreen.ChatScreen(
            isMessageActionModeState = isMessageActionModeState
        ).route -> PrimaryTopBar(
            title = if (currentNavRoute == NavigationScreen.ChatScreen(
                    isMessageActionModeState = isMessageActionModeState
                ).route
            ) currentChatState.value?.name
                ?: LocalStringResources.current.APP_NAME else settingsScreenNameByRoute(
                currentNavRoute, LocalStringResources.current
            ),
            onNavigationIconClick = onNavigationIconClick,
            isActionVisible = currentNavRoute == NavigationScreen.ChatScreen(
                isMessageActionModeState = isMessageActionModeState
            ).route && chatsState.value.orEmpty().isNotEmpty()
        ) {
            showEditChatDialog.value = true
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
fun DrawerHeader(isSettingsDrawerMode: Boolean, onDrawerModeClick: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(color = Primary700),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Image(
                painter = painterResource(if (isSettingsDrawerMode) Res.drawable.ic_settings else Res.drawable.avatar_ai),
                contentDescription = LocalStringResources.current.SETTINGS,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp).size(60.dp)
            )
            Text(
                text = if (isSettingsDrawerMode) LocalStringResources.current.SETTINGS else LocalStringResources.current.APP_NAME,
                fontSize = 16.sp,
                color = Neutral50,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, top = 8.dp)
            )
        }
        IconButton(onClick = {
            onDrawerModeClick.invoke(isSettingsDrawerMode.not())
        }) {
            Image(
                painter = painterResource(if (isSettingsDrawerMode) Res.drawable.ic_chat else Res.drawable.ic_settings),
                contentDescription = LocalStringResources.current.NAVIGATION_ICON,
                modifier = Modifier.padding(end = 16.dp, top = 16.dp).size(24.dp)
            )
        }
    }
}

@Composable
fun AppSnackBar(snackBarHostState: SnackbarHostState) {
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