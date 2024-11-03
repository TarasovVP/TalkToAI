package presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vnteam.talktoai.presentation.NavigationScreens
import resources.LocalStringResources
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState

@Composable
fun AppNavigation(navController: NavHostController, screenState: MutableState<ScreenState>) {

    NavHost(navController = navController, startDestination = NavigationScreens.MainScreen.route) {
        composable(NavigationScreens.MainScreen.route) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = LocalStringResources.current.APP_NAME,
                    topAppBarActionVisible = false,
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = true,
                    floatingActionButtonTitle = LocalStringResources.current.ADD,
                    floatingActionButtonAction = {
                        navController.navigate("${NavigationScreens.CreateScreen.route}-1")
                    }
                )
            )

        }
        composable("${NavigationScreens.DetailsScreen.route}{demoObjectId}/{demoObjectName}", arguments = listOf(navArgument("demoObjectId") {
            type = NavType.StringType
            defaultValue = ""
        }, navArgument("demoObjectName") {
            type = NavType.StringType
            defaultValue = ""
        })) { backStackEntry ->
            val demoObjectId = backStackEntry.arguments?.getString("demoObjectId").orEmpty()
            val demoObjectName = backStackEntry.arguments?.getString("demoObjectName").orEmpty()
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = demoObjectName,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.popBackStack()
                    }
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = true,
                    floatingActionButtonTitle = LocalStringResources.current.EDIT,
                    floatingActionButtonAction = {
                        navController.navigate("${NavigationScreens.CreateScreen.route}$demoObjectId")
                    }
                )
            )

        }
        composable("${NavigationScreens.CreateScreen.route}{demoObjectId}", arguments = listOf(navArgument("demoObjectId") {
            type = NavType.StringType
            defaultValue = ""
            nullable = true
        })) { backStackEntry ->
            val demoObjectId = backStackEntry.arguments?.getString("demoObjectId").takeIf { it?.isNotEmpty() == true && it != "-1"} ?: ""
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = if (demoObjectId.isNotEmpty()) LocalStringResources.current.EDIT else LocalStringResources.current.CREATE,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                floatingActionButtonVisible = false
            ))

        }
    }
}