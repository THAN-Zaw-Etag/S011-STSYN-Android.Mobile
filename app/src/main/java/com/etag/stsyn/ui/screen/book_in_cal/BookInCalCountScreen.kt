package com.etag.stsyn.ui.screen.book_in_cal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseCountScreen

@Composable
fun BookInCalCountScreen(
    items: List<String>, modifier: Modifier = Modifier
) {

    BaseCountScreen(
        itemCount = items.size,
        modifier = modifier,
        onTabSelected = { controlType ->
            // filter items here
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) {
                ScannedItem(id = "Hello", name = "World", showTrailingIcon = true)
            }
        }
    }
}