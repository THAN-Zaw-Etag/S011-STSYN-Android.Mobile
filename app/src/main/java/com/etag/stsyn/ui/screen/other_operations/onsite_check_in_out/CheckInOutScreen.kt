package com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ErrorText
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseScanScreen

@Composable
fun CheckInOutScreen(
    onsiteCheckInOutViewModel: OnsiteCheckInOutViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by onsiteCheckInOutViewModel.rfidUiState.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(rfidUiState.scannedItems) {
        if (rfidUiState.scannedItems.size > 1) listState.animateScrollToItem(rfidUiState.scannedItems.size - 1)
    }

    Column(modifier = modifier.fillMaxSize()) {
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = "Note: Issuer and receiver must be from the same flight"
        )
        BaseScanScreen(
            scannedItemCount = rfidUiState.scannedItems.size,
            onScan = { onsiteCheckInOutViewModel.toggle() },
            isScanning = rfidUiState.isScanning,
            onClear = { onsiteCheckInOutViewModel.removeScannedItems() }) {
            LazyColumn(
                modifier = Modifier,
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(rfidUiState.scannedItems.size) {
                    key(it) {
                        ScannedItem(
                            id = "$it",
                            isSwipeable = true,
                            name = "data link jumper cable".uppercase(),
                            onSwipeToDismiss = {}
                        )
                    }
                }
            }
        }
    }
}