package com.etag.stsyn.ui.screen.book_out.book_out_box

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.screen.base.BaseBoxScreen

@Composable
fun BookOutBoxScanScreen(
    bookOutBoxViewModel: BookOutBoxViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookOutBoxViewModel.rfidUiState.collectAsStateWithLifecycle()
    val boxUiState by bookOutBoxViewModel.boxUiState.collectAsStateWithLifecycle()
    val scannedItemList by bookOutBoxViewModel.scannedItemList.collectAsStateWithLifecycle()

    BaseBoxScreen(
        bookItems = boxUiState.allItemsOfBox,
        isScanning = rfidUiState.isScanning,
        modifier = modifier,
        checked = boxUiState.isChecked,
        scannedItemList = scannedItemList,
        scannedBox = boxUiState.scannedBox,
        onCheckChange = bookOutBoxViewModel::toggleVisualCheck,
        onRefresh = bookOutBoxViewModel::refreshScannedBox,
        onScan = bookOutBoxViewModel::toggle,
        onReset = bookOutBoxViewModel::resetAllItemsInBox
    )
}