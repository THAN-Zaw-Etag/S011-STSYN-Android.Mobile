package com.etag.stsyn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.etag.stsyn.core.BaseViewModel.ScanType
import com.etag.stsyn.ui.screen.main.HomeScreen
import com.etag.stsyn.ui.screen.main.LoginContentScreen
import com.etag.stsyn.ui.screen.main.LoginScreen
import com.etag.stsyn.ui.screen.main.LoginViewModel
import com.etag.stsyn.ui.screen.main.SplashScreen
import com.etag.stsyn.ui.viewmodel.RfidViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    rfidViewModel: RfidViewModel,
) {

    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginUiState by loginViewModel.loginUiState.collectAsState()

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = Routes.SplashScreen.name
    ) {

        composable(route = Routes.SplashScreen.name) {

            SplashScreen(
                onTimeOut = {
                    navController.navigate(Routes.LoginScreen.name) {
                        popUpTo(Routes.SplashScreen.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Routes.LoginScreen.name) {
            loginViewModel.updateScanType(ScanType.Single)

            LoginScreen(
                navigateToLoginContentScreen = { navController.navigate(Routes.LoginContentScreen.name) },
                loginViewModel = loginViewModel
            )
        }

        composable(route = Routes.LoginContentScreen.name) {
            LaunchedEffect(loginUiState.isLoginSuccessful) {
                if (loginUiState.isLoginSuccessful)
                    navController.navigate(Routes.HomeContentScreen.name)
            }
            LoginContentScreen(
                isSuccessful = loginUiState.isLoginSuccessful,
                errorMessage = loginUiState.errorMessage,
                onLogInClick = { loginViewModel.login(it) }
            )
        }

        composable(route = Routes.HomeContentScreen.name) {
            HomeScreen(
                rfidViewModel = rfidViewModel,
                onLogOutClick = {},
                onSettingsClick = {},
            )
        }

    }
}