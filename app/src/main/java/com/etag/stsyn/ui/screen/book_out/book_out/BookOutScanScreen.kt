package com.etag.stsyn.ui.screen.book_out.book_out

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseScanScreen
import com.etag.stsyn.util.DataSource

@Composable
fun BookOutScanScreen(
    bookOutViewModel: BookOutViewModel,
    modifier: Modifier = Modifier
) {
    val scannedItems = remember { mutableStateListOf(String()) }
    //val sharedUiViewModel = LocalRfidViewModel.current

    LaunchedEffect(Unit) {
        DataSource.dummyDataList.forEach {
            scannedItems.add(it)
        }
    }

    BaseScanScreen(
        scannedItemCount = scannedItems.count(),
        modifier = modifier,
        onScan = { /*sharedUiViewModel.startScan()*/ },
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