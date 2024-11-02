package presentation.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.vnteam.talktoai.data.generateUUID
import com.vnteam.talktoai.presentation.intents.CreateIntent
import com.vnteam.talktoai.presentation.resources.DrawableResources
import com.vnteam.talktoai.presentation.states.CreateViewState
import com.vnteam.talktoai.presentation.states.screen.ScreenState
import com.vnteam.talktoai.presentation.uimodels.DemoObjectUI
import com.vnteam.talktoai.presentation.uimodels.OwnerUI
import com.vnteam.talktoai.presentation.viewmodels.CreateViewModel
import org.koin.compose.viewmodel.koinViewModel
import presentation.components.ChangeAvatarDialog

@Composable
fun CreateScreen(demoObjectId: String, screenState: MutableState<ScreenState>,
                 content: @Composable (State<CreateViewState>, originDemoObject: MutableState<DemoObjectUI?>, onClick: () -> Unit) -> Unit) {

    val viewModel = koinViewModel<CreateViewModel>()
    val viewState = viewModel.state.collectAsState()
    val originDemoObject = remember {  mutableStateOf<DemoObjectUI?>(null) }

    LaunchedEffect(Unit) {
        if (demoObjectId.isNotEmpty()) {
            viewModel.processIntent(CreateIntent.LoadDemoObject(demoObjectId))
        } else {
            viewModel.state.value.demoObject = mutableStateOf(
                DemoObjectUI(
                    demoObjectId = generateUUID(),
                    owner = OwnerUI(ownerId = generateUUID())
                )
            )
        }
    }
    LaunchedEffect(viewState.value) {
        originDemoObject.value = viewState.value.demoObject.value
    }
    LaunchedEffect(viewState.value.successResult) {
        if (viewState.value.successResult) {
            screenState.value = screenState.value.copy(isScreenUpdatingNeeded = true)
            screenState.value.appBarState.topAppBarAction.invoke()
            viewState.value.successResult = false
        }
    }

    content(viewState, originDemoObject) {
        viewState.value.demoObject.value?.let {
            viewModel.processIntent(CreateIntent.CreateDemoObject(it))
        }
    }
    screenState.value = screenState.value.copy()
    if (viewState.value.isChangeAvatarDialogVisible.value) {
        ChangeAvatarDialog(avatarList = DrawableResources.avatarList, onDismiss = {
            viewState.value.isChangeAvatarDialogVisible.value = false
        }, onClick = { avatar ->
            viewState.value.demoObject.value = viewState.value.demoObject.value?.copy(
                owner = viewState.value.demoObject.value?.owner?.copy(avatarUrl = avatar)
            )
            viewState.value.isChangeAvatarDialogVisible.value = false
        })
    }
}