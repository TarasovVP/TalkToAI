package com.vnstudio.talktoai.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.flagDrawable
import com.vnstudio.talktoai.infrastructure.Constants
import com.vnstudio.talktoai.infrastructure.Constants.CURRENT_CHAT_ID
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import com.vnstudio.talktoai.presentation.components.draggable.DragDropColumn
import com.vnstudio.talktoai.presentation.screens.chat.ChatScreen
import com.vnstudio.talktoai.presentation.screens.authorization.login.LoginScreen
import com.vnstudio.talktoai.presentation.screens.authorization.onboarding.OnboardingScreen
import com.vnstudio.talktoai.presentation.screens.authorization.signup.SignUpScreen
import com.vnstudio.talktoai.presentation.screens.authorization.signup.SignUpViewModel
import com.vnstudio.talktoai.presentation.sealed_classes.SettingsScreen
import com.vnstudio.talktoai.presentation.sealed_classes.SettingsScreen.Companion.isSettingsScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_account.SettingsAccountScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_chat.SettingsChatScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_feedback.SettingsFeedbackScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_language.SettingsLanguageScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_privacy_policy.SettingsPrivacyPolicyScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_sign_up.SettingsSignUpScreen
import com.vnstudio.talktoai.presentation.screens.settings.settings_theme.SettingsThemeScreen
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
fun DeleteModeTopBar(title: String) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = Primary900,
        contentColor = Neutral50)
}

@Composable
fun AppDrawer(
    isSettingsDrawerMode: MutableState<Boolean?>,
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
            if (chats.value.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.empty_state),
                    contentDescription = "Empty state",
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
                ) {chat, isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 4.dp else 1.dp)
                    DrawerItem(
                        name = chat.name,
                        mainIcon = R.drawable.ic_chat,
                        isCurrent = chat.id == currentChatId,
                        secondaryIcon = if (isDragging) R.drawable.ic_drag_handle else R.drawable.ic_delete,
                        elevation = elevation.value,
                        isIconClick = true,
                        onIconClick = {
                            if (isDragging.not()) onDeleteChatClick.invoke(chat)
                        },
                        onItemClick = { if (isDragging.not()) onChatClick.invoke(chat) })
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

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    isSettingsDrawerMode: MutableState<Boolean?>,
    isMessageDeleteModeState: MutableState<Boolean?>,
    infoMessageState: MutableState<InfoMessage?>,
    progressVisibilityState: MutableState<Boolean>,
) {
    NavHost(navController, startDestination = startDestination.takeIf { it.isNotEmpty() } ?: NavigationScreen.OnboardingScreen().route,
        enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }) {
        composable(
            route = NavigationScreen.OnboardingScreen().route
        ) {
            OnboardingScreen {
                navController.navigate(NavigationScreen.LoginScreen().route)
            }
        }
        composable(
            route = NavigationScreen.LoginScreen().route
        ) {
            LoginScreen(hiltViewModel(), infoMessageState, progressVisibilityState) { route ->
                navController.navigate(route)
            }
        }
        composable(
            route = NavigationScreen.SignUpScreen().route
        ) {
            SignUpScreen(hiltViewModel(), infoMessageState, progressVisibilityState) { route ->
                navController.navigate(route)
            }
        }
        composable(
            route = NavigationScreen.ChatScreen().route,
            arguments = listOf(navArgument(CURRENT_CHAT_ID) {
                type = NavType.LongType
                defaultValue = DEFAULT_CHAT_ID
            })
        ) { backStackEntry ->
            val currentChatId = backStackEntry.arguments?.getLong(CURRENT_CHAT_ID) ?: DEFAULT_CHAT_ID
            ChatScreen(currentChatId, isMessageDeleteModeState, infoMessageState, progressVisibilityState)
            BackHandler(true) {

            }
        }
        composable(
            route = NavigationScreen.SettingsChatScreen().route
        ) {
            SettingsChatScreen(infoMessageState, progressVisibilityState) {

            }
        }
        composable(
            route = NavigationScreen.SettingsAccountScreen().route
        ) {
            SettingsAccountScreen(infoMessageState, progressVisibilityState) { route ->
                isSettingsDrawerMode.value = null
                navController.navigate(route)
            }
        }
        composable(
            route = NavigationScreen.SettingsSignUpScreen().route
        ) {
            SettingsSignUpScreen(infoMessageState, progressVisibilityState) { route ->
                isSettingsDrawerMode.value = null
                navController.navigate(route)
            }
        }
        composable(
            route = NavigationScreen.SettingsLanguageScreen().route
        ) {
            SettingsLanguageScreen(infoMessageState, progressVisibilityState)
        }
        composable(
            route = NavigationScreen.SettingsThemeScreen().route
        ) {
            SettingsThemeScreen(infoMessageState)
        }
        composable(
            route = NavigationScreen.SettingsFeedbackScreen().route
        ) {
            SettingsFeedbackScreen(infoMessageState, progressVisibilityState)
        }
        composable(
            route = NavigationScreen.SettingsPrivacyPolicyScreen().route
        ) {
            SettingsPrivacyPolicyScreen(progressVisibilityState)
        }
    }
}