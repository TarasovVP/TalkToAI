package presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.presentation.viewmodels.AppViewModel
import kotlinx.coroutines.launch
import presentation.AppNavigation

@Composable
fun AppContent(appViewModel: AppViewModel) {

    // Scaffold
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val navController = rememberNavController()

    val screenState = appViewModel.screenState.collectAsState()

    println("AppContentTAG: screenState.value: ${screenState.value}")

    LaunchedEffect(screenState.value?.currentScreenRoute) {
        screenState.value?.currentScreenRoute?.let {
            navController.navigate(it)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = screenState.value?.isLoggedInUser.isTrue(),
        drawerContent = {
            DrawerContent(screenState.value) { newScreenState, isDrawerClose ->
                appViewModel.updateScreenState(newScreenState)
                if (isDrawerClose) {
                    scope.launch {
                        drawerState.close()
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(screenState.value) {
                    if (screenState.value?.isSecondaryScreen.isTrue()) {
                        navController.navigateUp()
                    } else {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            }
                        }
                    }
                }
            },
            snackbarHost = {
                AppSnackBar(screenState.value, scope)
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                screenState.value?.let { screenState ->
                    AppNavigation(navController, screenState) { updatedScreenState ->
                        println("AppTAG AppNavigation updatedScreenState: $updatedScreenState")
                        appViewModel.updateScreenState(updatedScreenState)
                    }
                }
                if (screenState.value?.isProgressVisible.isTrue()) {
                    val animationResourceState = appViewModel.animationResource.collectAsState()
                    println("AppTAG AppNavigation screenState.value?.isProgressVisible.isTrue(): ${screenState.value?.isProgressVisible.isTrue()}")
                    appViewModel.animationUtils.MainProgressAnimation(
                        animationResourceState.value.orEmpty()
                    )
                }
            }
        }
    }
}