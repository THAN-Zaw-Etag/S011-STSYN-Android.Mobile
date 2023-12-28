package com.etag.stsyn.ui.screen.book_in.t_loan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ExpandedScannedItem
import com.etag.stsyn.ui.screen.base.BaseCountScreen

@Composable
fun TLoanCountScreen(
    items: List<String>
) {
    BaseCountScreen(itemCount = items.size, onTabSelected = {}) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items) {
                ExpandedScannedItem()
            }
        }
    }
}