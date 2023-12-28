@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.other_operations.det_p_loan_box

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.etag.stsyn.util.DataSource
import kotlinx.coroutines.launch

@Composable
fun OtherDetPLoanOutBoxCountScreen() {
    val items = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        DataSource.dummyDataList.forEach {
            items.add(it)
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(skipPartiallyExpanded = false, skipHiddenState = false)
    )
    val coroutineScope = rememberCoroutineScope()

    BaseCountScreen(itemCount = items.size, onTabSelected = {}) {
        ScannedItem(id = "Hello", name = "World", showTrailingIcon = true, onItemClick = {
            if (scaffoldState.bottomSheetState.isVisible) coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
            else coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
        })
    }
}