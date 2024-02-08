package com.etag.stsyn.ui.navigation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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


@Composable
fun HomeNavigationGraph(
    isSodInitiate: Boolean,
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
        route = Graph.HOME,
        navController = navController,
        startDestination = Routes.HomeScreen.name,
        modifier = modifier
    ) {

        composable(route = Routes.HomeScreen.name) {
            var isInitiate by remember { mutableStateOf(isSodInitiate) }

            val popResultFlow by it.savedStateHandle.getStateFlow("isSodInitiate",true).collectAsState()

            LaunchedEffect(popResultFlow) {
                if (!popResultFlow) isInitiate = false
            }

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
                shiftType = loginUiState.shift,
                isSodInitiate = isInitiate,
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
        composable(route = Routes.SettingsScreen.name) {
            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.SettingsScreen.title)
                updateTopAppBarStatus(true)
                updateAppBarNavigationIcon(Icons.Default.ArrowBack)
                updateBottomNavigationBarStatus(false)
            }

            SettingsScreen()
        }
        detailsNavGraph(navController,sharedUiViewModel,loginViewModel)
        authNavGraph(
            navController = navController,
            loginViewModel = loginViewModel,
            context)
    }
}

fun NavGraphBuilder.detailsNavGraph(
    navController: NavHostController,
    sharedUiViewModel:SharedUiViewModel,
    loginViewModel: LoginViewModel
    ) {
    navigation(
        route = Graph.DETAILS,
        startDestination = Routes.DetailScreen.name
    ) {
        composable(route = Routes.DetailScreen.name + "/{type}") {
            val optionType = OptionType.valueOf(
                it.arguments?.getString("type") ?: OptionType.BookOut.toString()
            )
            val rfidUiState by loginViewModel.rfidUiState.collectAsState()
            val loginUiState by loginViewModel.loginUiState.collectAsState()

            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.OtherOperationsScreen.title)
                updateTopAppBarStatus(false)
                updateAppBarNavigationIcon(Icons.Default.Menu)
                updateBottomNavigationBarStatus(false)
            }

            DetailScreen(
                isConnected = rfidUiState.isConnected,
                optionType = optionType,
                shiftType = loginUiState.shift,
                logOut = {
                    loginViewModel.logOut()
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Routes.DetailScreen.name) {
                            inclusive = true
                        }
                    }
                },
                navigateToHomeScreen = {
                    navController.navigate(Routes.HomeScreen.name)
                },
                navigateToMainMenu = {
                    // after saving
                    try {
                        navController.navigateUp()
                        navController.currentBackStackEntry?.savedStateHandle?.set("isSodInitiate",false)
                    }catch (e: Exception) {e.printStackTrace()}
                }
            )
        }

    }
}