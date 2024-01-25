package com.etag.stsyn.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.etag.stsyn.core.BaseViewModel.ScanType
import com.etag.stsyn.ui.screen.login.LoginContentScreen
import com.etag.stsyn.ui.screen.login.LoginScreen
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.screen.main.HomeScreen
import com.etag.stsyn.ui.screen.main.SplashScreen
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.domain.model.user.UserModel
import kotlinx.coroutines.delay

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel
) {

    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val savedUser by loginViewModel.savedUser.collectAsState(LocalUser())
    val epcModelUserName by loginViewModel.epcModelUser.collectAsState(UserModel())

    LaunchedEffect(navController.currentDestination) {
        if ((navController.currentDestination ?: "") == Routes.LoginScreen.name) {
            loginViewModel.updateScanType(ScanType.Single)
        }
    }

    LaunchedEffect(savedUser.isLoggedIn) {
        delay(1000)
    }

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = Routes.LoginContentScreen.name
    ) {

        composable(route = Routes.SplashScreen.name) {
            SplashScreen(
                onTimeOut = {
                    navController.navigate(
                        if (savedUser.isLoggedIn) Routes.HomeContentScreen.name
                        else Routes.LoginScreen.name
                    ) {
                        popUpTo(Routes.SplashScreen.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Routes.LoginScreen.name) {
            loginViewModel.enableScan()
            LoginScreen(
                navigateToLoginContentScreen = { navController.navigate(Routes.LoginContentScreen.name) },
                loginViewModel = loginViewModel
            )
        }

        composable(route = Routes.LoginContentScreen.name) {
            loginViewModel.removeListener() // remove listener when

            BackHandler(true) {
                loginViewModel.navigateToScanScreen()
                navController.navigateUp()
            }

            LaunchedEffect(loginUiState.isLoginSuccessful) {
                if (loginUiState.isLoginSuccessful)
                    navController.navigate(Routes.HomeContentScreen.name)
            }

            val loginResponse by loginViewModel.loginResponse.collectAsState()

            LoginContentScreen(
                loginAttemptCount = loginUiState.attemptCount,
                userName = epcModelUserName.userName ,
                loginResponse = loginResponse,
                isSuccessful = loginUiState.isLoginSuccessful,
                onLogInClick = { loginViewModel.login(it.toCharArray()) },
                onSuccess = loginViewModel::saveUserToLocalStorage,
                onFailed = {
                    loginViewModel.apply {
                        increaseLoginAttempt()
                        updateLoginStatus(false)
                    }
                }
            )
            BackHandler {
                navController.navigate(Routes.LoginScreen.name)
            }
        }

        composable(route = Routes.HomeContentScreen.name) {
            // disable scan
            loginViewModel.disableScan()

            HomeScreen(
                loginViewModel = loginViewModel,
                onChangePassword = loginViewModel::updatePassword,
                onLogOutClick = {
                    loginViewModel.logOut()
                    navController.navigate(Routes.LoginScreen.name) {
                        popUpTo(Routes.HomeContentScreen.name) {
                            inclusive = true
                        }
                    }
                },
                onSettingsClick = {},
            )
        }
    }
}