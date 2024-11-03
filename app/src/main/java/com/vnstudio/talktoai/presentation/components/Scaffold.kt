package com.vnstudio.talktoai.presentation.components

import android.util.Log
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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen.Companion.isSettingsScreen
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen.Companion.settingScreens
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen.Companion.settingsScreenNameByRoute
import com.vnstudio.talktoai.flagDrawable
import com.vnstudio.talktoai.infrastructure.Constants
import com.vnstudio.talktoai.presentation.components.draggable.DragDropColumn
import com.vnstudio.talktoai.presentation.theme.Neutral50
import com.vnstudio.talktoai.presentation.theme.Primary100
import com.vnstudio.talktoai.presentation.theme.Primary700
import com.vnstudio.talktoai.presentation.theme.Primary800
import com.vnstudio.talktoai.presentation.theme.Primary900
import com.vnstudio.talktoai.resources.DrawableResources

@Composable
fun PrimaryTopBar(
    title: String,
    onNavigationIconClick: () -> Unit,
    isActionVisible: Boolean,
    onActionIconClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = Primary900,
        contentColor = Neutral50,
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    painter = painterRes(DrawableResources.IC_NAVIGATION),
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
                        painter = painterRes(DrawableResources.IC_EDIT),
                        contentDescription = LocalStringResources.current.CHAT_EDIT_BUTTON,
                        tint = Primary100
                    )
                }
            }
        }
    )
}

@Composable
fun SecondaryTopBar(title: String, onNavigationIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = Primary900,
        contentColor = Neutral50,
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    painter = painterRes(DrawableResources.IC_ARROW_BACK),
                    contentDescription = LocalStringResources.current.NAVIGATION_ICON
                )
            }
        })
}

@Composable
fun DeleteModeTopBar(title: String) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = Primary900,
        contentColor = Neutral50
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
                        mainIcon = settingsScreen.icon,
                        isCurrent = currentRouteState == settingsScreen.route,
                        secondaryIcon = if (settingsScreen.route == NavigationScreen.SettingsLanguageScreen().route) LocalConfiguration.current.locales.flagDrawable() else null
                    ) {
                        onNextScreen.invoke(settingsScreen.route)
                    }
                }
            }
        } else {
            if (chats.value.isNullOrEmpty()) {
                Image(
                    painter = painterRes(DrawableResources.EMPTY_STATE),
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
                        mainIcon = DrawableResources.IC_CHAT,
                        isCurrent = chat.id == currentChatId,
                        secondaryIcon = if (isDragging) DrawableResources.IC_DRAG_HANDLE else DrawableResources.IC_DELETE,
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
                DrawableResources.IC_CHAT_ADD,
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
                painter = painterRes(if (isSettingsDrawerMode) DrawableResources.IC_SETTINGS else DrawableResources.AVATAR_AI),
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
                painter = painterRes(if (isSettingsDrawerMode) DrawableResources.IC_CHAT else DrawableResources.IC_SETTINGS),
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
    mainIcon: String,
    isCurrent: Boolean,
    secondaryIcon: String? = null,
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
        backgroundColor = if (isCurrent) Primary800 else Primary900,
        elevation = elevation ?: 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterRes(mainIcon), contentDescription = name,
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
                Image(painter = painterRes(icon),
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
                    backgroundColor = if (data.actionLabel == Constants.ERROR_MESSAGE) Color.Red else Primary700
                ) {
                    Text(data.message)
                }
                Spacer(modifier = Modifier.fillMaxSize())
            }
        }
    )
}