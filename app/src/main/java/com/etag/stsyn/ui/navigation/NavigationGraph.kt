package com.etag.stsyn.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.etag.stsyn.core.BaseViewModel.ScanType
import com.etag.stsyn.data.model.LocalUser
import com.etag.stsyn.ui.components.ExitApp
import com.etag.stsyn.ui.screen.login.LoginContentScreen
import com.etag.stsyn.ui.screen.login.LoginScreen
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.screen.main.HomeScreen
import com.etag.stsyn.ui.screen.main.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
) {
    LaunchedEffect(navController.currentDestination) {
        if ((navController.currentDestination ?: "") == Routes.LoginScreen.name) {
            loginViewModel.updateScanType(ScanType.Single)
        }
    }

    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val rfidUiState by loginViewModel.rfidUiState.collectAsState()
    val savedUser by loginViewModel.savedUser.collectAsState(LocalUser())
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState(initial = false)

    LaunchedEffect(isLoggedIn) {
        Log.d("TAG", "isLoggedIn: $isLoggedIn")
    }

    // set scan type to single when current destination is login screen

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = if (isLoggedIn) Routes.HomeScreen.name else Routes.SplashScreen.name
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
            LoginScreen(
                navigateToLoginContentScreen = { navController.navigate(Routes.LoginContentScreen.name) },
                loginViewModel = loginViewModel
            )
        }

        composable(route = Routes.LoginContentScreen.name) {
            loginViewModel.removeListener() // remove listener when
            LaunchedEffect(loginUiState.isLoginSuccessful) {
                if (loginUiState.isLoginSuccessful)
                    navController.navigate(Routes.HomeContentScreen.name)
            }

            val loginResponse by loginViewModel.loginResponse.collectAsState()

            LoginContentScreen(
                loginAttemptCount = loginUiState.attemptCount,
                userName = savedUser.name,
                loginResponse = loginResponse,
                isSuccessful = loginUiState.isLoginSuccessful,
                errorMessage = loginUiState.errorMessage,
                onLogInClick = { loginViewModel.login(it) },
                onSuccess = loginViewModel::saveUserToLocalStorage,
                onFailed = {
                    loginViewModel.apply {
                        increaseLoginAttempt()
                        updateLoginStatus(false)
                    }
                }
            )
        }

        composable(route = Routes.HomeContentScreen.name) {
            val context = LocalContext.current

            HomeScreen(
                loginViewModel = loginViewModel,
                onChangePassword = { old, new -> },
                onLogOutClick = { ExitApp(context) },
                onSettingsClick = {},
            )
        }
    }
}