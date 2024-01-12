package com.etag.stsyn.ui.navigation

import android.util.Log
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
import com.etag.stsyn.core.BaseViewModel.ScanType
import com.etag.stsyn.data.model.User
import com.etag.stsyn.ui.components.ExitApp
import com.etag.stsyn.ui.screen.main.HomeScreen
import com.etag.stsyn.ui.screen.main.LoginContentScreen
import com.etag.stsyn.ui.screen.main.LoginScreen
import com.etag.stsyn.ui.screen.main.LoginViewModel
import com.etag.stsyn.ui.screen.main.SplashScreen
import com.etag.stsyn.ui.viewmodel.RfidViewModel

const val TAG = "NavigationGraph"

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    rfidViewModel: RfidViewModel,
) {

    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val rfidUiState by loginViewModel.rfidUiState.collectAsState()

    // set scan type to single when current destination is login screen
    LaunchedEffect(navController.currentDestination) {
        Log.d(TAG, "NavigationGraph: ${rfidUiState.scanType}")
        if ((navController.currentDestination ?: "") == Routes.LoginScreen.name) {
            loginViewModel.updateScanType(ScanType.Single)
        }
    }

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
            loginViewModel.removeListener()
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
            val savedUserState by loginViewModel.savedUser.collectAsState(initial = User())
            val context = LocalContext.current

            HomeScreen(
                savedUserState = savedUserState,
                rfidViewModel = rfidViewModel,
                onLogOutClick = { ExitApp(context) },
                onSettingsClick = {},
            )
        }

    }
}