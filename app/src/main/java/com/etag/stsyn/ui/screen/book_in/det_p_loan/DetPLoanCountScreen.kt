package com.etag.stsyn.ui.screen.book_in.det_p_loan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ExpandedScannedItem
import com.etag.stsyn.ui.screen.base.BaseCountScreen

@Composable
fun DetPLoanCountScreen(
    detPLoanViewModel: DetPLoanViewModel,
    modifier: Modifier = Modifier
) {
    var items = remember { mutableStateListOf<String>() }
    BaseCountScreen(itemCount = items.size, onTabSelected = {}) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(10) {
                ExpandedScannedItem()
            }
        }
    }
}