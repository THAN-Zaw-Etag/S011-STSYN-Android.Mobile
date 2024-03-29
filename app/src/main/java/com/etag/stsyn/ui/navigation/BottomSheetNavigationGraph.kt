package com.etag.stsyn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.etag.stsyn.ui.screen.bottomsheet.BoxDetailScreen
import com.etag.stsyn.ui.screen.bottomsheet.BoxListBottomSheetScreen
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem

sealed class BottomSheetNavigation(val name: String) {
    object BoxList : BottomSheetNavigation("box_list_screen")
    object BoxDetail : BottomSheetNavigation("box_detail_screen")
}

@Composable
fun BottomSheetNavigationGraph(
    navController: NavHostController = rememberNavController(),
    boxList: List<BoxItem>
) {
    NavHost(navController = navController, startDestination = BottomSheetNavigation.BoxList.name) {
        composable(route = BottomSheetNavigation.BoxList.name) {
            BoxListBottomSheetScreen(boxItems = boxList, onItemClick = { index ->
                navController.navigate("${BottomSheetNavigation.BoxDetail.name}/$index")
            })
        }

        composable(route = "${BottomSheetNavigation.BoxDetail.name}/{index}") {
            val index = it.arguments?.getString("index")?.toInt() ?: 0
            val box = boxList[index]

            BoxDetailScreen(boxItem = box)
        }
    }
}