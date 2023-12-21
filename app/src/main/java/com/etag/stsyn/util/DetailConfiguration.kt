package com.etag.stsyn.util

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.etag.stsyn.domain.model.TabOption
import com.etag.stsyn.ui.screen.book_out.BookOutSaveScreen
import com.etag.stsyn.ui.screen.book_out.BookOutScreen

@Composable
fun DetailConfigurationGraph(options: List<TabOption>, tabTitle: String) {
    return when (tabTitle) {
        options.get(0).title -> BookOutScreen(scannedItems = emptyList(), onClear = {})
        options.get(1).title -> BookOutSaveScreen()
        else -> Column() {}
    }
}