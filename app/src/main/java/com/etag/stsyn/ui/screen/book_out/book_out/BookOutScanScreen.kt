package com.etag.stsyn.ui.screen.book_out.book_out

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
fun BookOutScanScreen(
    bookOutViewModel: BookOutViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookOutViewModel.rfidUiState.collectAsState()
    val listState = rememberLazyListState()
    //var previousSize by remember { mutableStateOf(rfidUiState.scannedItems.size) }

    LaunchedEffect(rfidUiState.scannedItems) {

        if (rfidUiState.scannedItems.size > 1) listState.animateScrollToItem(rfidUiState.scannedItems.size - 1)
    }

    BaseScanScreen(
        scannedItemCount = rfidUiState.scannedItems.size,
        modifier = modifier,
        isScanning = rfidUiState.isScanning,
        onScan = { bookOutViewModel.toggle() },
        onClear = { rfidUiState.scannedItems.toMutableList().clear() }) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(rfidUiState.scannedItems) {
                key(it) {
                    ScannedItem(
                        id = it, name = "",
                        isSwipeable = true,
                        onSwipeToDismiss = {
                            bookOutViewModel.removeItem(it)
                        }
                    )
                }
            }
        }
    }
}