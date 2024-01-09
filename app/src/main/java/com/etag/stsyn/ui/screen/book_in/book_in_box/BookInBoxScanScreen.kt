@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.book_in.book_in_box

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.screen.book_out.book_out_box.BoxScreen

@Composable
fun BookInBoxScanScreen(
    bookInBoxViewModel: BookInBoxViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookInBoxViewModel.rfidUiState.collectAsState()

    BoxScreen(
        scannedItems = rfidUiState.scannedItems,
        onReset = { bookInBoxViewModel.removeScannedItems() },
        onScan = { bookInBoxViewModel.toggle() },
        isScanning = rfidUiState.isScanning,
        modifier = modifier,
        showBoxBookOutButton = true,
        boxOutTitle = "Box booked out (${rfidUiState.scannedItems.size})",
    )
}