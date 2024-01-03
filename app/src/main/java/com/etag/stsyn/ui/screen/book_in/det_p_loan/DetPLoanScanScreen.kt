package com.etag.stsyn.ui.screen.book_in.det_p_loan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseScanScreen

@Composable
fun DetPLoanScanScreen(
    detPLoanViewModel: DetPLoanViewModel,
    modifier: Modifier = Modifier
) {
    var scannedItems = remember { mutableStateListOf(String()) }
    BaseScanScreen(
        scannedItemCount = scannedItems.size,
        onScan = { /*TODO*/ },
        onClear = { scannedItems.clear() }) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(10) {
                key(it) {
                    ScannedItem(
                        id = "Hello",
                        name = "World",
                        isSwipeable = true,
                        onSwipeToDismiss = {})
                }
            }
        }
    }
}