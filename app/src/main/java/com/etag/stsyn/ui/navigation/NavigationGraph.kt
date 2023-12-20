package com.etag.stsyn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.etag.stsyn.ui.screen.HomeContent
import com.etag.stsyn.ui.screen.LoginScreen
import com.etag.stsyn.ui.screen.MainScreen
import com.etag.stsyn.ui.screen.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
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
                onLogOutClick = {},
                onSettingsClick = {},
            )
        }

    }
}