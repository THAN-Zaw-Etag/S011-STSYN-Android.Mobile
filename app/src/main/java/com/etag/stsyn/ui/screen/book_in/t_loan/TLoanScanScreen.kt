package com.etag.stsyn.ui.screen.book_in.t_loan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseScanScreen

@Composable
fun TLoanScanScreen(
    modifier: Modifier = Modifier
) {
    var scannedItems by remember { mutableStateOf(listOf<String>()) }

    BaseScanScreen(
        scannedItemCount = scannedItems.size,
        onScan = { /*TODO*/ },
        onClear = { /*TODO*/ }) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(scannedItems) {
                ScannedItem(id = "Hello", name = "World", isSwipeable = true)
            }
        }
    }
}