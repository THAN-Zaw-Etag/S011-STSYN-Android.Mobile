package com.etag.stsyn.ui.screen.book_out.book_out

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.LocalRfidViewModel
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseScanScreen

@Composable
fun BookOutScanScreen(
    bookOutViewModel: BookOutViewModel,
    modifier: Modifier = Modifier
) {
    val scannedItems = remember { mutableStateListOf(String()) }
    //val sharedUiViewModel = LocalRfidViewModel.current
    val rfidViewModel = LocalRfidViewModel.current
    val rfidUiState by rfidViewModel.rfidUiState.collectAsState()
    val bookOutUiState by bookOutViewModel.bookOutUiState.collectAsState()

    LaunchedEffect(rfidUiState.isScanned) {
        Log.d("TAG", "BookOutScanScreen: ${rfidUiState.isScanned}")
    }

    LaunchedEffect(bookOutUiState.scannedItems) {
        bookOutUiState.scannedItems.forEach {
            scannedItems.add(it)
        }
    }

    BaseScanScreen(
        scannedItemCount = scannedItems.count(),
        modifier = modifier,
        onScan = { bookOutViewModel.startScan() },
        onClear = { scannedItems.clear() }) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(scannedItems) {
                ScannedItem(id = "Hello", name = "World", isSwipeable = true)
            }
        }
    }
}