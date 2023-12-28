package com.etag.stsyn.ui.screen.book_out.book_out_box

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.etag.stsyn.util.DataSource

@Composable
fun BookOutBoxScanScreen() {
    val scannedItems = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        DataSource.dummyDataList.forEach {
            scannedItems.add(it)
        }
    }

    BoxScreen(
        scannedItems = scannedItems,
        onReset = { scannedItems.clear() }
    )
}