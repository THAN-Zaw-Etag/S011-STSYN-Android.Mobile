package com.etag.stsyn.ui.screen.book_in.book_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ExpandedScannedItem
import com.etag.stsyn.ui.screen.base.BaseCountScreen

@Composable
fun BookInCountScreen(
    viewModel: BookInViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.scannedBookInItems.collectAsState()

    BaseCountScreen(itemCount = items.size, onTabSelected = {}) {
        LazyColumn(
            modifier = Modifier,
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) {
                ExpandedScannedItem(
                    bookInItem = it
                )
            }
        }
    }
}