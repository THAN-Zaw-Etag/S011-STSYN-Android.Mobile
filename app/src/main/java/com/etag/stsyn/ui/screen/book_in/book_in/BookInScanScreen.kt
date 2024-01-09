package com.etag.stsyn.ui.screen.book_in.book_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseScanScreen

@Composable
fun BookInScanScreen(
    bookInViewModel: BookInViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookInViewModel.rfidUiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(rfidUiState.scannedItems) {
        if (rfidUiState.scannedItems.size > 1) listState.animateScrollToItem(rfidUiState.scannedItems.size - 1)
    }

    BaseScanScreen(
        modifier = modifier,
        scannedItemCount = rfidUiState.scannedItems.size,
        isScanning = rfidUiState.isScanning,
        onScan = { bookInViewModel.toggle() },
        onClear = { bookInViewModel.removeScannedItems() }) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(rfidUiState.scannedItems) {
                key(it) {
                    ScannedItem(
                        id = it,
                        name = "World",
                        isSwipeable = true,
                        onSwipeToDismiss = { bookInViewModel.removeItem(it) }
                    )
                }
            }
        }
    }
}