package com.etag.stsyn.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.etag.stsyn.enums.OptionType
import com.etag.stsyn.ui.components.ExitApp
import com.etag.stsyn.ui.screen.detail.DetailScreen
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.screen.main.BookInScreen
import com.etag.stsyn.ui.screen.main.BookOutScreen
import com.etag.stsyn.ui.screen.main.MainScreen
import com.etag.stsyn.ui.screen.main.OtherOperationsScreen
import com.etag.stsyn.ui.screen.settings.SettingsScreen
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel
import com.etag.stsyn.util.logout

@Composable
fun HomeNavigationGraph(
    loginViewModel: LoginViewModel,
    sharedUiViewModel: SharedUiViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val rfidUiState by loginViewModel.rfidUiState.collectAsState()
    val menuAccessRights by loginViewModel.userMenuAccessRight.collectAsState()
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen.name,
        modifier = modifier
    ) {
        composable(route = Routes.HomeScreen.name) {

            BackHandler(enabled = true) {
                ExitApp(context)
            }

            // update ui
            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.HomeScreen.title)
                updateTopAppBarStatus(true)
                updateAppBarNavigationIcon(Icons.Default.Menu)
                updateBottomNavigationBarStatus(false)
            }

            MainScreen(
                isReaderConnected = rfidUiState.isConnected,
                batteryPercentage = rfidUiState.batteryLevel,
                onCategoryItemClick = {
                    // save current selected bottom navigation route
                    navController.navigate(it)
                    sharedUiViewModel.apply {
                        updateBottomNavigationSelectedItem(it)
                    }
                })
        }

        composable(route = Routes.BookOutScreen.name) {
            BackHandler(true) {
                navController.navigate(Routes.HomeScreen.name)
            }
            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.BookOutScreen.title)
                updateTopAppBarStatus(true)
                updateAppBarNavigationIcon(Icons.Default.Menu)
                updateBottomNavigationBarStatus(true)
            }
            BookOutScreen(
                menuAccessRight = menuAccessRights,
                onOptionButtonClick = {
                    navController.navigate(Routes.DetailScreen.name + "/$it")
                }
            )
        }

        composable(route = Routes.BookInScreen.name) {
            BackHandler(true) {
                navController.navigate(Routes.HomeScreen.name)
            }
            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.BookInScreen.title)
                updateTopAppBarStatus(true)
                updateAppBarNavigationIcon(Icons.Default.Menu)
                updateBottomNavigationBarStatus(true)
            }
            BookInScreen(
                menuAccessRight = menuAccessRights,
                onOptionButtonClick = { optionType ->
                    navController.navigate(Routes.DetailScreen.name + "/$optionType")
                })
        }

        composable(route = Routes.OtherOperationsScreen.name) {
            BackHandler(true) {
                navController.navigate(Routes.HomeScreen.name)
            }
            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.OtherOperationsScreen.title)
                updateTopAppBarStatus(true)
                updateAppBarNavigationIcon(Icons.Default.Menu)
                updateBottomNavigationBarStatus(true)
            }
            OtherOperationsScreen(
                menuAccessRight = menuAccessRights,
                onOptionButtonClick = { optionType ->
                    navController.navigate(Routes.DetailScreen.name + "/$optionType")
                })
        }

        composable(route = Routes.DetailScreen.name + "/{type}") {
            val optionType = OptionType.valueOf(
                it.arguments?.getString("type") ?: OptionType.BookOut.toString()
            )

            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.OtherOperationsScreen.title)
                updateTopAppBarStatus(false)
                updateAppBarNavigationIcon(Icons.Default.Menu)
                updateBottomNavigationBarStatus(false)
            }

            DetailScreen(
                optionType = optionType,
                logOut = {
                    loginViewModel.logOut()
                    logout(navController, Routes.LoginScreen.name)
                },
                navigateToHomeScreen = {
                    navController.navigate(Routes.HomeScreen.name)
                }
            )
        }

        composable(route = Routes.SettingsScreen.name) {
            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.SettingsScreen.title)
                updateTopAppBarStatus(true)
                updateAppBarNavigationIcon(Icons.Default.ArrowBack)
                updateBottomNavigationBarStatus(false)
            }

            SettingsScreen()
        }
    }
}