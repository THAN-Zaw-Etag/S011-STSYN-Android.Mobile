@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.book_in.book_in_cal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.domain.model.ItemDetail
import com.etag.stsyn.ui.components.DetailRow
import com.etag.stsyn.ui.components.InfoBottomSheetContent
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.components.ScreenWithBottomSheet
import com.etag.stsyn.ui.screen.base.BaseScanScreen

@Composable
fun BookInCalScanScreen(
    bookInCalViewModel: BookInCalViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookInCalViewModel.rfidUiState.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(rfidUiState.scannedItems) {
        if (rfidUiState.scannedItems.size > 1) listState.animateScrollToItem(rfidUiState.scannedItems.size - 1)
    }

    ScreenWithBottomSheet(
        show = showBottomSheet,
        onDismiss = { showBottomSheet = false },
        modifier = modifier,
        sheetContent = { BookInCalScanBottomSheetContent("Title") }) {
        BaseScanScreen(
            scannedItemCount = rfidUiState.scannedItems.size,
            onScan = { bookInCalViewModel.toggle() },
            isScanning = rfidUiState.isScanning,
            onClear = { bookInCalViewModel.removeScannedItems() }
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = listState,
                contentPadding = PaddingValues(16.dp)
            ) {
                items(rfidUiState.scannedItems) {
                    ScannedItem(
                        id = it,
                        name = "john smith",
                        showTrailingIcon = true,
                        onItemClick = {
                            showBottomSheet = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BookInCalScanBottomSheetContent(
    title: String,
    modifier: Modifier = Modifier
) {
    InfoBottomSheetContent(title = title, modifier = modifier) {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(10) {
                DetailRow(itemDetail = ItemDetail("Hello", "World"))
            }
        }
    }
}