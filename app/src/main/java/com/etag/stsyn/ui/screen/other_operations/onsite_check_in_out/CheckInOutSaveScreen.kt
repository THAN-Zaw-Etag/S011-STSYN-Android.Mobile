package com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.book_in.book_in.BookInSaveScreen

@Composable
fun CheckInOutSaveScreen() {
    val hasScannedItems by remember { mutableStateOf(false) }
    BookInSaveScreen(showSaveButton = hasScannedItems, content = {
        SaveItemLayout(
            icon = Icons.Default.Person,
            itemTitle = "Receiver",
            showRefreshIcon = hasScannedItems
        ) {
            Text(text = "-")
        }
    }) {

    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun CheckInOutSaveScreenPreview() {
    CheckInOutSaveScreen()
}