package com.etag.stsyn.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.etag.stsyn.ui.components.ExitAppOnBackPress
import com.etag.stsyn.ui.screen.detail.DetailScreen
import com.etag.stsyn.ui.screen.main.BookInScreen
import com.etag.stsyn.ui.screen.main.BookOutScreen
import com.etag.stsyn.ui.screen.main.HomeScreen
import com.etag.stsyn.ui.screen.main.OtherOperationsScreen
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel

@Composable
fun HomeNavigationGraph(
    sharedUiViewModel: SharedUiViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen.name,
        modifier = modifier
    ) {
        composable(route = Routes.HomeScreen.name) {
            val context = LocalContext.current

            BackHandler(enabled = true) {
                ExitAppOnBackPress(context)
            }

            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.HomeScreen.title)
                updateTopAppBarStatus(true)
                updateBottomNavigationBarStatus(false)
            }

            HomeScreen(onCategoryItemClick = {
                // save current selected bottom navigtion route

                navController.navigate(it)
                sharedUiViewModel.apply {
                    updateBottomNavigationSelectedItem(it)
                }
            })
        }

        composable(route = Routes.BookOutScreen.name) {
            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.BookOutScreen.title)
                updateTopAppBarStatus(true)
                updateBottomNavigationBarStatus(true)
            }
            BookOutScreen(
                onOptionButtonClick = {}
            )
        }

        composable(route = Routes.BookInScreen.name) {
            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.BookInScreen.title)
                updateTopAppBarStatus(true)
                updateBottomNavigationBarStatus(true)
            }
            BookInScreen(onOptionButtonClick = { optionType ->
                // handle action according option type
            })
        }

        composable(route = Routes.OtherOperationsScreen.name) {
            BackHandler(true) {
                navController.navigate(Routes.HomeScreen.name)
            }
            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.OtherOperationsScreen.title)
                updateTopAppBarStatus(true)
                updateBottomNavigationBarStatus(true)
            }
            OtherOperationsScreen(onOptionButtonClick = { optionType ->

            })
        }

        composable(route = Routes.DetailScreen.name) {
            sharedUiViewModel.apply {
                updateTopBarTitle(Routes.OtherOperationsScreen.title)
                updateTopAppBarStatus(false)
                updateBottomNavigationBarStatus(false)
            }

            DetailScreen()
        }
    }
}