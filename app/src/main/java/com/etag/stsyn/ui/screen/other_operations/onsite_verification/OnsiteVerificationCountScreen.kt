@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.etag.stsyn.ui.screen.other_operations.onsite_verification

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ControlType
import com.etag.stsyn.ui.components.DetailBottomSheetScaffold
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.BottomSheetContent
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.util.ApiResponse
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnsiteVerificationCountScreen(
    onsiteVerificationViewModel: OnsiteVerificationViewModel,
    modifier: Modifier = Modifier
) {

    val scannedItems by onsiteVerificationViewModel.totalScannedItems.collectAsState()
    val itemsResponse by onsiteVerificationViewModel.getOnSiteVerifyItems.collectAsState()
    val outstandingItems by onsiteVerificationViewModel.outstandingItems.collectAsState()
    var items by remember { mutableStateOf<List<BoxItem>>(emptyList()) }

    val onsiteVerificationUiState by onsiteVerificationViewModel.onsiteVerificationUiState.collectAsState()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(skipPartiallyExpanded = true, skipHiddenState = false)
    )
    val coroutineScope = rememberCoroutineScope()
    DetailBottomSheetScaffold(
        state = scaffoldState,
        sheetContent = { BottomSheetContent(itemList = listOf()) }) {
        BaseCountScreen(
            itemCount = items.size,
            onTabSelected = {controlType->
                when (controlType) {
                    ControlType.All -> {
                        if (itemsResponse is ApiResponse.Success) {
                            items = onsiteVerificationUiState.allItemsFromApi
                        }
                    }

                    ControlType.Done -> {
                        items = scannedItems.map { it!! }
                    }

                    ControlType.Outstanding -> {
                        items = outstandingItems.map { it!! }
                    }
                }

            }


        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items) {
                    ScannedItem(id = it.epc, name = it.description, onItemClick = {
                        if (scaffoldState.bottomSheetState.isVisible) coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                        else coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                    })
                }
            }
        }
    }
}