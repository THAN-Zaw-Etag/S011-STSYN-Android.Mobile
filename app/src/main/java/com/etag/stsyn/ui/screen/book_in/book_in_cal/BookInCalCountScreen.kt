package com.etag.stsyn.ui.screen.book_in.book_in_cal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseCountScreen

@Composable
fun BookInCalCountScreen(
    bookInCalViewModel: BookInCalViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookInCalViewModel.rfidUiState.collectAsStateWithLifecycle()
    val items by remember {
        mutableStateOf(listOf<String>())
    }
    BaseCountScreen(
        itemCount = items.size,
        modifier = modifier,
        onTabSelected = { _ ->
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