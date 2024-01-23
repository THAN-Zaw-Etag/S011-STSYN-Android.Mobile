package com.etag.stsyn.ui.screen.book_in.book_in

import android.util.Log
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
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun BookInScanScreen(
    bookInViewModel: BookInViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookInViewModel.rfidUiState.collectAsState()
    val listState = rememberLazyListState()
    val bookInItemsResponse by bookInViewModel.bookInItemsResponse.collectAsState()
    val bookInItems by bookInViewModel.scannedBookInItems.collectAsState()

    LaunchedEffect(bookInItems) {
        Log.d("TAG", "BookInScanScreen: $bookInItems")
    }

    when (bookInItemsResponse) {
        is ApiResponse.Loading -> {
            bookInViewModel.toggleLoadingVisibility(true)
        }

        is ApiResponse.Success -> {
            bookInViewModel.toggleLoadingVisibility(false)
        }

        is ApiResponse.ApiError -> {
            bookInViewModel.toggleLoadingVisibility(false)
        }

        else -> {
            bookInViewModel.toggleLoadingVisibility(false)
        }
    }

    LaunchedEffect(rfidUiState.scannedItems) {
        if (rfidUiState.scannedItems.size > 1) listState.animateScrollToItem(rfidUiState.scannedItems.size - 1)
    }

    BaseScanScreen(
        modifier = modifier,
        scannedItemCount = bookInItems.size,
        isScanning = rfidUiState.isScanning,
        onScan = { bookInViewModel.toggle() },
        onClear = { bookInViewModel.removeAllScannedItems() }) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(bookInItems) {
                key(it?.epc) {
                    ScannedItem(
                        id = it?.epc ?: "",
                        name = it?.description ?: "",
                        isSwipeable = true,
                        onSwipeToDismiss = { bookInViewModel.removeScannedBookInItem(it!!) }
                    )
                }
            }
        }
    }
}