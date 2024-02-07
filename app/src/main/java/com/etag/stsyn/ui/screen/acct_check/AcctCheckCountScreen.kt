@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.etag.stsyn.ui.screen.acct_check

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.etag.stsyn.ui.screen.bottomsheet.BoxDetailScreen
import com.etag.stsyn.util.datasource.DataSource
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AcctCheckCountScreen(
    accountCheckViewModel: AccountCheckViewModel,
    modifier: Modifier = Modifier
) {
    val accountCheckUiState by accountCheckViewModel.acctCheckUiState.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(skipPartiallyExpanded = true, skipHiddenState = false)
    )
    var selectedItem by remember { mutableStateOf(BoxItem()) }
    var controlType by remember { mutableStateOf(ControlType.All) }
    var items by remember { mutableStateOf<List<BoxItem>>(emptyList()) }
    val scannedItemIdList by accountCheckViewModel.scannedItemIdList.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(controlType) {
        withContext(Dispatchers.IO) {
            items = when (controlType) {
                ControlType.All -> accountCheckUiState.allItems
                ControlType.Done -> accountCheckUiState.allItems.filter { it.epc in scannedItemIdList }
                ControlType.Outstanding -> accountCheckUiState.allItems.filter { it.epc !in scannedItemIdList }
            }
        }
    }

    DetailBottomSheetScaffold(
        state = scaffoldState,
        modifier = modifier,
        sheetContent = { BoxDetailScreen(boxItem = selectedItem) }) {
        BaseCountScreen(itemCount = items.size, onTabSelected = {
            controlType = it
        }) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(items) {
                    ScannedItem(
                        id = "${it.serialNo}-${it.partNo}",
                        name = it.description,
                        showTrailingIcon = true,
                        onItemClick = {
                            selectedItem = it
                            if (scaffoldState.bottomSheetState.isVisible) coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                            else coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                        }
                    )
                }
            }
        }
    }
}