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
    val scannedItems by bookOutViewModel.items.collectAsState()
    val bookOutUiState by bookOutViewModel.bookOutUiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(scannedItems) {
        if (scannedItems.size > 1) listState.animateScrollToItem(scannedItems.size - 1)
    }

    BaseScanScreen(
        scannedItemCount = scannedItems.size,
        modifier = modifier,
        onScan = { bookOutViewModel.toggle() },
        onClear = { scannedItems.toMutableList().clear() }) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(scannedItems) {
                key(it) {
                    ScannedItem(
                        id = it, name = "",
                        isSwipeable = true,
                        onSwipeToDismiss = {
                            bookOutViewModel.removeItem(it)
                        })
                }
            }
        }
    }
}