package com.etag.stsyn.ui.screen.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.InfoBottomSheetContent
import com.etag.stsyn.ui.screen.book_out.book_out_box.BookOutBoxItem

@Composable
fun BoxListBottomSheetScreen(
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    InfoBottomSheetContent(title = "List of booked out (Box)") {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(5) {
                BookOutBoxItem(onClick = onItemClick)
            }
        }
    }
}