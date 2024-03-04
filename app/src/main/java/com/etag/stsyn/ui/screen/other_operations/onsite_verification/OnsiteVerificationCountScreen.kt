@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.etag.stsyn.ui.screen.other_operations.onsite_verification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ControlType
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.components.ScreenWithBottomSheet
import com.etag.stsyn.ui.components.listItems
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.etag.stsyn.ui.screen.bottomsheet.BoxDetailScreen
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.util.ApiResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnsiteVerificationCountScreen(
    onsiteVerificationViewModel: OnsiteVerificationViewModel,
    ) {

    val scannedItems by onsiteVerificationViewModel.totalScannedItems.collectAsState()
    val itemsResponse by onsiteVerificationViewModel.getOnSiteVerifyItems.collectAsState()
    val outstandingItems by onsiteVerificationViewModel.outstandingItems.collectAsState()
    var items by remember { mutableStateOf<List<BoxItem>>(emptyList()) }
    var selectedItem by remember { mutableStateOf(BoxItem()) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val onsiteVerificationUiState by onsiteVerificationViewModel.onsiteVerificationUiState.collectAsState()

    ScreenWithBottomSheet(
        show = showBottomSheet,
        onDismiss = {showBottomSheet = false},
        sheetContent = { BoxDetailScreen(boxItem = selectedItem) }) {
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
                listItems(items) {
                    ScannedItem(id = it.epc, name = it.description, showTrailingIcon = true, onItemClick = {
                        selectedItem = it
                        showBottomSheet = true
                    })
                }
            }
        }
    }
}