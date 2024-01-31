package com.etag.stsyn.ui.screen.book_out.book_out_box

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.screen.base.BaseBoxScreen

@Composable
fun BookOutBoxScanScreen(
    bookOutBoxViewModel: BookOutBoxViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookOutBoxViewModel.rfidUiState.collectAsState()
    val boxUiState by bookOutBoxViewModel.boxUiState.collectAsState()

    BaseBoxScreen(
        bookItems = emptyList(),
        isScanning = rfidUiState.isScanning,
        modifier = modifier,
        scannedBox = boxUiState.scannedBox,
        onScan = bookOutBoxViewModel::toggle,
        onReset = { bookOutBoxViewModel.removeScannedItems() }
    )
}