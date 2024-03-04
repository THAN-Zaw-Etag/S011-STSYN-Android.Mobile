package com.etag.stsyn.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

@Composable
fun BottomSheetContent(itemList: List<String>) {
    LazyColumn {
        items(itemList) {

        }
    }
}
