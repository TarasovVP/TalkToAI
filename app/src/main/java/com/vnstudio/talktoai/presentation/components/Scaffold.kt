package com.vnstudio.talktoai.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.flagDrawable
import com.vnstudio.talktoai.presentation.screens.sealed_classes.SettingsScreen
import com.vnstudio.talktoai.presentation.theme.*

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
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_navigation),
                    contentDescription = "Navigation icon",
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
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit title",
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
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Navigation icon"
                )
            }
        })
}

@Composable
fun TopAppBarTitle(route: String?): String {
    return when (route) {
        NavigationScreen.SettingsChatScreen().route -> stringResource(id = R.string.settings_chat)
        NavigationScreen.SettingsAccountScreen().route -> stringResource(id = R.string.settings_account)
        NavigationScreen.SettingsLanguageScreen().route -> stringResource(id = R.string.settings_language)
        NavigationScreen.SettingsThemeScreen().route -> stringResource(id = R.string.settings_theme)
        NavigationScreen.SettingsPrivacyPolicyScreen().route -> stringResource(id = R.string.settings_privacy_policy)
        else -> stringResource(id = R.string.app_name)
    }
}

@Composable
fun AppDrawer(
    isSettingsDrawerMode: MutableState<Boolean?>,
    currentRouteState: String?,
    chats: MutableState<List<Chat>>,
    onCreateChatClick: () -> Unit,
    onChatClick: (Chat) -> Unit,
    onDeleteChatClick: (Chat) -> Unit,
    infoMessageState: MutableState<InfoMessage?>,
    onNextScreen: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary900),
        Arrangement.Top
    ) {
        DrawerHeader(
            isSettingsDrawerMode.value.isTrue() || NavigationScreen.isSettingsScreen(
                currentRouteState
            )
        ) { settingsDrawerMode ->
            isSettingsDrawerMode.value = settingsDrawerMode
        }
        if (isSettingsDrawerMode.value.isTrue() || NavigationScreen.isSettingsScreen(
                currentRouteState
            )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                SettingsScreen.allSettingsScreens.forEach { settingsScreen ->
                    DrawerItem(
                        name = stringResource(id = settingsScreen.name),
                        mainIcon = settingsScreen.icon,
                        isCurrent = currentRouteState == settingsScreen.route,
                        secondaryIcon = if (settingsScreen.route == NavigationScreen.SettingsLanguageScreen().route) LocalConfiguration.current.locales.flagDrawable() else null
                    ) {
                        onNextScreen.invoke(settingsScreen.route)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                items(chats.value) { chat ->
                    DrawerItem(
                        name = chat.name,
                        mainIcon = R.drawable.ic_chat,
                        isCurrent = chats.value.indexOf(chat) == 0,
                        secondaryIcon = R.drawable.ic_delete,
                        isIconClick = true,
                        onIconClick = {
                            onDeleteChatClick.invoke(chat)
                        },
                        onItemClick = { onChatClick.invoke(chat) })
                }
            }
            TextIconButton(
                "Новый чат",
                R.drawable.ic_chat_add,
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
                imageVector = ImageVector.vectorResource(id = if (isSettingsDrawerMode) R.drawable.ic_settings else R.drawable.avatar_ai),
                contentDescription = "Settings item icon",
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .size(60.dp)
            )
            Text(
                text = if (isSettingsDrawerMode) "Настройки" else "Искусcтвенный интеллект",
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
                imageVector = ImageVector.vectorResource(id = if (isSettingsDrawerMode) R.drawable.ic_chat else R.drawable.ic_settings),
                contentDescription = "Drawer item icon",
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
    mainIcon: Int,
    isCurrent: Boolean,
    secondaryIcon: Int? = null,
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
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = mainIcon), contentDescription = name,
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
                Image(imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = name,
                    modifier = Modifier
                        .padding(8.dp).let {
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