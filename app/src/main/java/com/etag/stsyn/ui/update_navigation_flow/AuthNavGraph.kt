package com.etag.stsyn.ui.update_navigation_flow

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.etag.stsyn.ui.components.ExitApp
import com.etag.stsyn.ui.navigation.Routes
import com.etag.stsyn.ui.navigation.detailsNavGraph
import com.etag.stsyn.ui.screen.login.LoginContentScreen
import com.etag.stsyn.ui.screen.login.LoginScreen
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.screen.main.SplashScreen
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel


fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    loginViewModel:LoginViewModel,
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
        composable(route = Routes.LoginScreen.name) {
            loginViewModel.enableScan()
            LoginScreen(
                navigateToLoginContentScreen = { navController.navigate(Routes.LoginContentScreen.name) },
                loginViewModel = loginViewModel
            )
            BackHandler {
                ExitApp(context)
            }
        }
        composable(route = Routes.LoginContentScreen.name) {
            loginViewModel.removeListener() // remove listener when

            BackHandler(true) {
                loginViewModel.navigateToScanScreen()
                navController.navigateUp()
            }

            val logInState = loginViewModel.loginUiState.collectAsState()
//            LaunchedEffect(logInState) {
//                if (logInState.value.isLoginSuccessful)
//                    navController.navigate(Graph.HOME)
//            }

            val loginResponse by loginViewModel.loginResponse.collectAsState()

            val epcModelUserName= loginViewModel.epcModelUser.collectAsState()
            LoginContentScreen(
                goToHome = {
                    navController.navigate(Graph.HOME) {
                        popUpTo(Routes.LoginContentScreen.name) {
                            inclusive = true
                        }
                    }
                },
                loginAttemptCount = logInState.value.attemptCount,
                userName = epcModelUserName.value.userName ,
                loginResponse = loginResponse,
                isSuccessful = logInState.value.isLoginSuccessful,
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

