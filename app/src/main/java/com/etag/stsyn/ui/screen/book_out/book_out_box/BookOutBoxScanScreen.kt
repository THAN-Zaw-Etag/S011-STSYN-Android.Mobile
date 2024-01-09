package com.etag.stsyn.ui.screen.book_out.book_out_box

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun BookOutBoxScanScreen(
    bookOutBoxViewModel: BookOutBoxViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookOutBoxViewModel.rfidUiState.collectAsState()

    BoxScreen(
        scannedItems = rfidUiState.scannedItems,
        isScanning = rfidUiState.isScanning,
        modifier = modifier,
        onScan = { bookOutBoxViewModel.toggle() },
        onReset = { bookOutBoxViewModel.removeScannedItems() }
    )
}