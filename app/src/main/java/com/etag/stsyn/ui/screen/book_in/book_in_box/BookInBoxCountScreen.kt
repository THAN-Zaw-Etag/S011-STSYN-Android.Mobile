package com.etag.stsyn.ui.screen.book_in.book_in_box

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
fun BookInBoxCountScreen(
    bookInBoxViewModel: BookInBoxViewModel,
    modifier: Modifier = Modifier
) {
    val bookInBoxUiState by bookInBoxViewModel.bookInBoxUiState.collectAsState()


    BaseCountScreen(itemCount = bookInBoxUiState.allBoxes.size, onTabSelected = {}) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(bookInBoxUiState.allBoxes) {
                ExpandedScannedItem(null)
            }
        }
    }
}