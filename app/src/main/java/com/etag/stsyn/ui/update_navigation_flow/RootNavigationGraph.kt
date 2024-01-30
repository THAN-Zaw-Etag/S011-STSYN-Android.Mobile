package com.etag.stsyn.ui.update_navigation_flow

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.etag.stsyn.ui.navigation.Routes
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.screen.main.HomeScreen
import com.etag.stsyn.util.AppUtil

@Composable
fun RootNavigationGraph(
    isLoggedIn: Boolean,
    navController: NavHostController,
    loginViewModel: LoginViewModel,
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {
        authNavGraph(
            navController = navController,
            loginViewModel = loginViewModel,
            context
        )
        composable(route = Graph.HOME) {
            // disable scan
            loginViewModel.disableScan()

            HomeScreen(
                loginViewModel = loginViewModel,
                onChangePassword = loginViewModel::updatePassword,
                onLogOutClick = {
                    loginViewModel.logOut()
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.HOME) {
                            inclusive = true
                        }
                    }


                    AppUtil.logout(navController, Routes.LoginScreen.name)
                },
                onSettingsClick = {},
            )
        }

    }
    LaunchedEffect(isLoggedIn) {
        Log.d("@InRoot", "MainActivity: savedUser: $isLoggedIn")
        if (isLoggedIn) {
            navController.navigateToSingleTop(Graph.HOME)
        }
    }
}


fun NavController.navigateToSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}