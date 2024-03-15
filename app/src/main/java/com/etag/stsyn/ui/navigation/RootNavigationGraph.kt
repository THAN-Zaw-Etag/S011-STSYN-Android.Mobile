package com.etag.stsyn.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.screen.main.HomeScreen
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel
import kotlinx.coroutines.delay

private const val TAG = "RootNavigationGraph"


@Composable
fun RootNavigationGraph(
    isLoggedIn: Boolean,
    navController: NavHostController,
    sharedUiViewModel: SharedUiViewModel,
    loginViewModel: LoginViewModel,
) {
    val context = LocalContext.current
    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()
    val isSodInitiate by loginViewModel.isSodInitiate.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {
        authNavGraph(
            navController = navController,
            loginViewModel = loginViewModel,
            context = context
        )
        composable(route = "${Graph.HOME}/{isSodInitiate}") {
            // disable scan
            loginViewModel.disableScan()

            val isInitiate = it.arguments?.getString("isSodInitiate").toBoolean()

            HomeScreen(
                loginViewModel = loginViewModel,
                onChangePassword = loginViewModel::updatePassword,
                sharedUiViewModel = sharedUiViewModel,
                isSodInitiate = isInitiate,
                onLogOutClick = {
                    loginViewModel.logOut()
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
    LaunchedEffect(isLoggedIn) {
        Log.d(TAG, "RootNavigationGraph: $isSodInitiate")
        if (isLoggedIn) {
            val route = if (isSodInitiate == false) "${Graph.HOME}/${false}" else Routes.SODInitiateScreen.name
            delay(300)
            navController.navigateToSingleTop(route)
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