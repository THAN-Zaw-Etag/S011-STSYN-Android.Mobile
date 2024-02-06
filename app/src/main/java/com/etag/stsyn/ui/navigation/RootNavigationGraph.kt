package com.etag.stsyn.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.screen.main.HomeScreen


@Composable
fun RootNavigationGraph(
    isLoggedIn: Boolean,
    navController: NavHostController,
    loginViewModel: LoginViewModel,
) {
    val context = LocalContext.current
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {
        authNavGraph(
            navController = navController,
            loginViewModel = loginViewModel,
            context,

            )
        composable(route = "${Graph.HOME}/{isSodInitiate}") {
            // disable scan
            loginViewModel.disableScan()

            val isSodInitiate = it.arguments?.getString("isSodInitiate").toBoolean()

            HomeScreen(
                loginViewModel = loginViewModel,
                onChangePassword = loginViewModel::updatePassword,
                isSodInitiate = isSodInitiate,
                onLogOutClick = {
                    loginViewModel.logOut()
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                onSettingsClick = {},
            )
        }
    }
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigateToSingleTop(if (loginUiState.isSodInitiate) Routes.SODInitiateScreen.name else "${Graph.HOME}/${false}")
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