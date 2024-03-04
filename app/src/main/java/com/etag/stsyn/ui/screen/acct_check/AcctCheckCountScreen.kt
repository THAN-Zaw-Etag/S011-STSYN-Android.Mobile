@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.etag.stsyn.ui.screen.acct_check

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.components.ControlType
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.components.ScreenWithBottomSheet
import com.etag.stsyn.ui.components.listItems
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.etag.stsyn.ui.screen.bottomsheet.BoxDetailScreen
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcctCheckCountScreen(
    accountCheckViewModel: AccountCheckViewModel,
    modifier: Modifier = Modifier
) {
    val accountCheckUiState by accountCheckViewModel.acctCheckUiState.collectAsStateWithLifecycle()
    var selectedItem by remember { mutableStateOf(BoxItem()) }
    var controlType by remember { mutableStateOf(ControlType.All) }
    var items by remember { mutableStateOf<List<BoxItem>>(emptyList()) }
    val scannedItemIdList by accountCheckViewModel.scannedItemIdList.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(controlType) {
        items = when (controlType) {
            ControlType.All -> accountCheckUiState.allItems
            ControlType.Done -> accountCheckUiState.allItems.filter { it.epc in scannedItemIdList }
            ControlType.Outstanding -> accountCheckUiState.allItems.filter { it.epc !in scannedItemIdList }
        }
    }

    ScreenWithBottomSheet(
        show = showBottomSheet,
        modifier = modifier,
        onDismiss = { showBottomSheet = false },
        sheetContent = { BoxDetailScreen(boxItem = selectedItem) }) {
        BaseCountScreen(itemCount = items.size, onTabSelected = {
            controlType = it
        }) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                listItems(items) {
                    key(it.epc) {
                        ScannedItem(
                            id = "${it.serialNo}-${it.itemLocation}",
                            name = it.description,
                            showTrailingIcon = true,
                            onItemClick = {
                                selectedItem = it
                                showBottomSheet = true
                            }
                        )
                    }
                }
            }
        }
    }
}