package com.etag.stsyn.ui.screen.book_in.book_in_box

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.book_in.BookInSaveScreen

@Composable
fun BookInBoxSaveScreen(
    onRefresh: () -> Unit,
    onSave: () -> Unit
) {
    BookInSaveScreen(showSaveButton = true, onSave = onSave, content = {
        SaveItemLayout(
            icon = Icons.Default.People,
            itemTitle = "Buddy",
            showRefreshIcon = true,
            onRefresh = onRefresh
        ) {
            Text(text = "-")
        }
    })
}