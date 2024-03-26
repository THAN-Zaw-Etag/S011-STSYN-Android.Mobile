package com.etag.stsyn.ui.navigation

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.etag.stsyn.util.MAXIMUM_LOGIN_ATTEMPTS
import com.tzh.retrofit_module.util.log.Logger


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
            val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()
            SplashScreen(
                onTimeOut = {
                    navController.navigate(
                        if (loginViewModel.savedUser.value.isLoggedIn && !loginState.isSodInitiate) Routes.HomeContentScreen.name
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

            val logInState by loginViewModel.loginState.collectAsStateWithLifecycle()
            val loginResponse by loginViewModel.loginResponse.collectAsStateWithLifecycle()
            val loginUiState by loginViewModel.loginState.collectAsStateWithLifecycle()
            val lockUserState by loginViewModel.lockUserState.collectAsStateWithLifecycle()
            val epcModelUserName by loginViewModel.epcModelUser.collectAsStateWithLifecycle()
            val savedUser by loginViewModel.savedUser.collectAsStateWithLifecycle()
            val isSodInitiate by loginViewModel.isSodInitiate.collectAsStateWithLifecycle(false)

            LaunchedEffect(savedUser.isLoggedIn) {
                Logger.d("authNavGraph: ${savedUser.isLoggedIn }")
            }
            
            LoginContentScreen(
                goToHome = {
                    // if sod initiate is true, go to sod initiate screen else home screen
//                    navController.navigate(if (false) Routes.SODInitiateScreen.name else Graph.HOME) {
//                        popUpTo(0) {
//                            inclusive = true
//                        }
//                    }

                    navController.navigate(if (isSodInitiate == true) Routes.SODInitiateScreen.name else "${Graph.HOME}/${isSodInitiate}") {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }

                    loginViewModel.resetLoginResponseState()
                },
                loginAttemptCount = logInState.attemptCount,
                name = epcModelUserName.userName,
                loginResponse = loginResponse,
                onLogInClick = {
                    if (loginUiState.attemptCount >= MAXIMUM_LOGIN_ATTEMPTS) {
                        loginViewModel.lockUser(epcModelUserName.nric)
                    } else {
                        loginViewModel.login(it.toCharArray())
                    }
                },
                onUpdatePassword = {old, new ->
                    loginViewModel.updatePassword(old.toCharArray(), new.toCharArray())
                },
                lockUserState = lockUserState
            )
            BackHandler {
                navController.navigate(Routes.LoginScreen.name)
            }
        }
    }

}

