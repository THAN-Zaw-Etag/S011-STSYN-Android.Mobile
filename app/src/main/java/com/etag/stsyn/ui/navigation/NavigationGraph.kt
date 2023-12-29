package com.etag.stsyn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.etag.stsyn.ui.screen.main.HomeContent
import com.etag.stsyn.ui.screen.main.LoginScreen
import com.etag.stsyn.ui.screen.main.MainScreen
import com.etag.stsyn.ui.screen.main.SplashScreen
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    sharedUiViewModel: SharedUiViewModel
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = Routes.SplashScreen.name
    ) {

        composable(route = Routes.SplashScreen.name) {
            SplashScreen(onTimeOut = {
                navController.navigate(Routes.HomeContentScreen.name)
            })
        }

        composable(route = Routes.MainScreen.name) {
            MainScreen()
        }

        composable(route = Routes.LoginScreen.name) {
            LoginScreen(
                onLogInClick = {}
            )
        }

        composable(route = Routes.HomeContentScreen.name) {
            HomeContent(
                sharedUiViewModel = sharedUiViewModel,
                onLogOutClick = {},
                onSettingsClick = {},
            )
        }

    }
}