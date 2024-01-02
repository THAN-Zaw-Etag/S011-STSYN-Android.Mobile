package com.etag.stsyn.ui.screen.book_in.t_loan

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
fun TLoanScanScreen(
    tLoanViewModel: TLoanViewModel,
    modifier: Modifier = Modifier
) {
    val scannedItems = remember { mutableStateListOf(String()) }

    LaunchedEffect(Unit) {
        DataSource.dummyDataList.forEach {
            scannedItems.add(it)
        }
    }

    BaseScanScreen(
        modifier = modifier,
        scannedItemCount = scannedItems.size,
        onScan = { /*TODO*/ },
        onClear = { /*TODO*/ }) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(scannedItems) {
                ScannedItem(id = "Hello", name = "World", isSwipeable = true)
            }
        }
    }
}