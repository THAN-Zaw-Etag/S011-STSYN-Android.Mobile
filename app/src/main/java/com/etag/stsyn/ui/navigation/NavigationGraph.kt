package com.etag.stsyn.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.etag.stsyn.ui.components.ExitAppOnBackPress
import com.etag.stsyn.ui.screen.main.HomeContent
import com.etag.stsyn.ui.screen.main.LoginScreen
import com.etag.stsyn.ui.screen.main.LoginViewModel
import com.etag.stsyn.ui.screen.main.MainScreen
import com.etag.stsyn.ui.screen.main.SplashScreen
import com.etag.stsyn.ui.viewmodel.RfidViewModel
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    rfidViewModel: RfidViewModel,
    sharedUiViewModel: SharedUiViewModel
) {
    val rfidUiState by rfidViewModel.rfidUiState.collectAsState()

    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginUiState by loginViewModel.loginUiState.collectAsState()

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = Routes.SplashScreen.name
    ) {

        composable(route = Routes.SplashScreen.name) {

            SplashScreen(onTimeOut = {
                navController.navigate(Routes.MainScreen.name)
            })
        }

        composable(route = Routes.MainScreen.name) {

            val context = LocalContext.current
            BackHandler(enabled = true) { ExitAppOnBackPress(context) }
            LaunchedEffect(rfidUiState.isScanned) {
                if (rfidUiState.isScanned) navController.navigate(Routes.LoginScreen.name)
            }
            MainScreen(
                onScan = {
                    rfidViewModel.startScan()
                }
            )
        }

        composable(route = Routes.LoginScreen.name) {
            LaunchedEffect(loginUiState.isLoginSuccessful) {
                if (loginUiState.isLoginSuccessful) navController.navigate(Routes.HomeContentScreen.name)
            }
            LoginScreen(
                isSuccessful = loginUiState.isLoginSuccessful,
                onLogInClick = { loginViewModel.updateLoginStatus(it.equals("1234")) }
            )
        }

        composable(route = Routes.HomeContentScreen.name) {
            HomeContent(
                rfidViewModel = rfidViewModel,
                sharedUiViewModel = sharedUiViewModel,
                onLogOutClick = {},
                onSettingsClick = {},
            )
        }

    }
}