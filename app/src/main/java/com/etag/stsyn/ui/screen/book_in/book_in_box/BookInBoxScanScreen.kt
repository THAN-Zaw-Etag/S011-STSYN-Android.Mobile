@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.book_in.book_in_box

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.screen.book_out.book_out_box.BoxScreen
import com.etag.stsyn.util.DataSource

@Composable
fun BookInBoxScanScreen(
    bookInBoxViewModel: BookInBoxViewModel,
    modifier: Modifier = Modifier
) {
    val items = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        DataSource.dummyDataList.forEach { items.add(it) }
    }

    BoxScreen(
        scannedItems = items,
        onReset = { /*TODO*/ },
        showBoxBookOutButton = true,
        boxOutTitle = "Box booked out (${items.size})",
    )
}