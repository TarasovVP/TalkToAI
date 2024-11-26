package presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnteam.talktoai.CommonExtensions.flagDrawable
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.avatar_ai
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.empty_state
import com.vnteam.talktoai.ic_arrow_back
import com.vnteam.talktoai.ic_chat
import com.vnteam.talktoai.ic_chat_add
import com.vnteam.talktoai.ic_delete
import com.vnteam.talktoai.ic_drag_handle
import com.vnteam.talktoai.ic_edit
import com.vnteam.talktoai.ic_navigation
import com.vnteam.talktoai.ic_settings
import com.vnteam.talktoai.presentation.ui.components.TextIconButton
import com.vnteam.talktoai.presentation.ui.components.draggable.DragDropColumn
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import presentation.NavigationScreen.Companion.isSettingsScreen
import presentation.NavigationScreen.Companion.settingScreens
import presentation.NavigationScreen.Companion.settingsScreenNameByRoute
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral50
import com.vnteam.talktoai.presentation.ui.theme.Primary100
import com.vnteam.talktoai.presentation.ui.theme.Primary700
import com.vnteam.talktoai.presentation.ui.theme.Primary800
import com.vnteam.talktoai.presentation.ui.theme.Primary900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTopBar(
    title: String,
    onNavigationIconClick: () -> Unit,
    isActionVisible: Boolean,
    onActionIconClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Primary900,
            titleContentColor = Neutral50
        ),
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_navigation),
                    contentDescription = LocalStringResources.current.NAVIGATION_ICON,
                    tint = Primary100
                )
            }
        },
        actions = {
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
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTopBar(title: String, onNavigationIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Primary900,
            titleContentColor = Neutral50
        ),
        navigationIcon = {
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
        title = { Text(title) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Primary900,
            titleContentColor = Neutral50
        )
    )
}

@Composable
fun AppDrawer(
    isSettingsDrawerMode: MutableState<Boolean>,
    currentRouteState: String?,
    currentChatId: Long?,
    chats: State<List<Chat>?>,
    onCreateChatClick: () -> Unit,
    onChatClick: (Chat) -> Unit,
    onDeleteChatClick: (Chat) -> Unit,
    onSwap: (Int, Int) -> Unit,
    onDragEnd: () -> Unit,
    onNextScreen: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary900),
        Arrangement.Top
    ) {
        DrawerHeader(
            isSettingsDrawerMode.value.isTrue() || isSettingsScreen(currentRouteState)
        ) { settingsDrawerMode ->
            isSettingsDrawerMode.value = settingsDrawerMode
        }
        if (isSettingsDrawerMode.value.isTrue() || isSettingsScreen(currentRouteState)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                settingScreens.forEach { settingsScreen ->
                    DrawerItem(
                        name = settingsScreenNameByRoute(settingsScreen.route, LocalStringResources.current),
                        mainIcon = Res.drawable.ic_settings,
                        isCurrent = currentRouteState == settingsScreen.route,
                        secondaryIcon = if (settingsScreen.route == NavigationScreen.SettingsLanguageScreen().route) LocaleList.current.flagDrawable() else null
                    ) {
                        onNextScreen.invoke(settingsScreen.route)
                    }
                }
            }
        } else {
            if (chats.value.isNullOrEmpty()) {
                Image(
                    painter = painterResource(Res.drawable.empty_state),
                    contentDescription = LocalStringResources.current.CHAT_EMPTY_STATE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 16.dp)
                )
            } else {
                DragDropColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    items = chats.value.orEmpty(),
                    onSwap = onSwap,
                    onDragEnd = onDragEnd
                ) { chat, isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 4.dp else 1.dp)
                    DrawerItem(
                        name = chat.name,
                        mainIcon = Res.drawable.ic_chat,
                        isCurrent = chat.id == currentChatId,
                        secondaryIcon = if (isDragging) Res.drawable.ic_drag_handle else Res.drawable.ic_delete,
                        elevation = elevation.value,
                        isIconClick = true,
                        onIconClick = {
                            if (isDragging.not()) onDeleteChatClick.invoke(chat)
                        },
                        onItemClick = { if (isDragging.not()) onChatClick.invoke(chat) })
                }
            }
            TextIconButton(
                LocalStringResources.current.NEW_CHAT,
                Res.drawable.ic_chat_add,
                Modifier.padding(bottom = 40.dp, start = 16.dp, end = 16.dp),
                onCreateChatClick
            )
        }
    }
}

@Composable
fun DrawerHeader(isSettingsDrawerMode: Boolean, onDrawerModeClick: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Primary700), verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Image(
                painter = painterResource(if (isSettingsDrawerMode) Res.drawable.ic_settings else Res.drawable.avatar_ai),
                contentDescription = LocalStringResources.current.SETTINGS,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .size(60.dp)
            )
            Text(
                text = if (isSettingsDrawerMode) LocalStringResources.current.SETTINGS else LocalStringResources.current.APP_NAME,
                fontSize = 16.sp,
                color = Neutral50,
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp, top = 8.dp)
            )
        }
        IconButton(onClick = {
            onDrawerModeClick.invoke(isSettingsDrawerMode.not())
        }) {
            Image(
                painter = painterResource(if (isSettingsDrawerMode) Res.drawable.ic_chat else Res.drawable.ic_settings),
                contentDescription = LocalStringResources.current.NAVIGATION_ICON,
                modifier = Modifier
                    .padding(end = 16.dp, top = 16.dp)
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun DrawerItem(
    name: String,
    mainIcon: DrawableResource,
    isCurrent: Boolean,
    secondaryIcon: DrawableResource? = null,
    elevation: Dp? = null,
    isIconClick: Boolean? = false,
    onIconClick: () -> Unit = {},
    onItemClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .let { modifier ->
                if (isCurrent.not()) {
                    modifier.clickable {
                        onItemClick.invoke()
                    }
                } else {
                    modifier
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) Primary800 else Primary900
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation ?: 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(mainIcon), contentDescription = name,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = name,
                color = Neutral50,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            )
            secondaryIcon?.let { icon ->
                Image(painter = painterResource(icon),
                    contentDescription = name,
                    modifier = Modifier
                        .padding(8.dp)
                        .let {
                            if (isIconClick.isTrue()) {
                                it.clickable {
                                    onIconClick.invoke()
                                }
                            } else {
                                it
                            }
                        })
            }
        }
    }
}

@Composable
fun AppSnackBar(snackBarHostState: SnackbarHostState) {
    SnackbarHost(
        hostState = snackBarHostState,
        snackbar = { data ->
            Box {
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    containerColor = if (data.visuals.actionLabel == Constants.ERROR_MESSAGE) Color.Red else Primary700
                ) {
                    Text(data.visuals.message)
                }
                Spacer(modifier = Modifier.fillMaxSize())
            }
        }
    )
}