package com.etag.stsyn.ui.navigation

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.etag.stsyn.ui.components.exitApp
import com.etag.stsyn.ui.screen.login.LoginContentScreen
import com.etag.stsyn.ui.screen.login.LoginScreen
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.screen.login.SODInitiateScreen
import com.etag.stsyn.ui.screen.main.SplashScreen


fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    context: Context
) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = Routes.LoginScreen.name
    ) {
        composable(route = Routes.SplashScreen.name) {
            SplashScreen(
                onTimeOut = {
                    navController.navigate(
                        if (loginViewModel.savedUser.value.isLoggedIn) Routes.HomeContentScreen.name
                        else Routes.LoginScreen.name
                    ) {
                        popUpTo(Routes.SplashScreen.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Routes.SODInitiateScreen.name) {
            SODInitiateScreen(
                onSodInitiate = {
                    navController.navigate("${Graph.HOME}/${true}")
                }
            )
        }

        composable(route = Routes.LoginScreen.name) {
            loginViewModel.enableScan()
            LoginScreen(
                navigateToLoginContentScreen = { navController.navigate(Routes.LoginContentScreen.name) },
                loginViewModel = loginViewModel
            )
            BackHandler {
                exitApp(context)
            }
        }
        composable(route = Routes.LoginContentScreen.name) {
            loginViewModel.removeListener() // remove listener

            BackHandler(true) {
                loginViewModel.navigateToScanScreen()
                navController.navigateUp()
            }

            val logInState = loginViewModel.loginUiState.collectAsState()
            val loginResponse by loginViewModel.loginResponse.collectAsState()
            val loginUiState by loginViewModel.loginUiState.collectAsState()
            val epcModelUserName = loginViewModel.epcModelUser.collectAsState()

            LoginContentScreen(
                goToHome = {
                    // if sod initiate is true, go to sod initiate screen else home screen
//                    navController.navigate(if (false) Routes.SODInitiateScreen.name else Graph.HOME) {
//                        popUpTo(0) {
//                            inclusive = true
//                        }
//                    }

                    navController.navigate(if (loginUiState.isSodInitiate) Routes.SODInitiateScreen.name else "${Graph.HOME}/${loginUiState.isSodInitiate}") {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }

                    loginViewModel.resetLoginResponseState()
                },
                loginAttemptCount = logInState.value.attemptCount,
                userName = epcModelUserName.value.userName,
                loginResponse = loginResponse,
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
    }

}

