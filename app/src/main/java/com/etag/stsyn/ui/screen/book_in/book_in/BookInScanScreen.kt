package com.etag.stsyn.ui.screen.book_in.book_in

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseScanScreen

@Composable
fun BookInScanScreen(
    bookInViewModel: BookInViewModel,
    modifier: Modifier = Modifier
) {
    val TAG = "BookInScanScreen"
    val rfidUiState by bookInViewModel.rfidUiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val bookInState by bookInViewModel.bookInState.collectAsStateWithLifecycle()
    val scannedItemIdList by bookInViewModel.scannedItemIdList.collectAsStateWithLifecycle()
    
    LaunchedEffect(scannedItemIdList) {
        Log.d(TAG, "BookInScanScreen: $scannedItemIdList")
    }

    BaseScanScreen(
        modifier = modifier,
        scannedItemCount = scannedItemIdList.size,
        isScanning = rfidUiState.isScanning,
        onScan = bookInViewModel::toggle,
        onClear = bookInViewModel::removeAllScannedItems) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(bookInState.allBookInItems.filter { it.epc in scannedItemIdList }) {
                key(it.epc) {
                    ScannedItem(
                        id = "${it.serialNo} - ${it.itemLocation}",
                        name = it.description,
                        isSwipeable = true,
                        onSwipeToDismiss = { bookInViewModel.removeScannedBookInItem(it.epc) }
                    )
                }
            }
        }
    }
}