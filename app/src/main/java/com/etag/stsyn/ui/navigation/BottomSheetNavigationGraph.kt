package com.etag.stsyn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.etag.stsyn.ui.screen.bottomsheet.BoxDetailScreen
import com.etag.stsyn.ui.screen.bottomsheet.BoxListBottomSheetScreen

sealed class BottomSheetNavigation(val name: String) {
    object BoxList : BottomSheetNavigation("box_list_screen")
    object BoxDetail : BottomSheetNavigation("box_detail_screen")
}

@Composable
fun BottomSheetNavigationGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = BottomSheetNavigation.BoxList.name) {
        composable(route = BottomSheetNavigation.BoxList.name) {
            BoxListBottomSheetScreen(onItemClick = { navController.navigate(BottomSheetNavigation.BoxDetail.name) })
        }

        composable(route = BottomSheetNavigation.BoxDetail.name) {
            BoxDetailScreen(title = "C123456")
        }
    }
}