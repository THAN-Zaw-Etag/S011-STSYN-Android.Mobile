package com.etag.stsyn.ui.screen.book_in.book_in_box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ControlType
import com.etag.stsyn.ui.components.ExpandedScannedItem
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.tzh.retrofit_module.data.mapper.toExpandedScannedItems

@Composable
fun BookInBoxCountScreen(
    bookInBoxViewModel: BookInBoxViewModel,
    modifier: Modifier = Modifier
) {
    val bookInBoxUiState by bookInBoxViewModel.bookInBoxUiState.collectAsState()
    var boxes by remember { mutableStateOf(bookInBoxUiState.allItemsOfBox) }
    var controlType by remember { mutableStateOf(ControlType.All) }

    LaunchedEffect(controlType) {
        val items = bookInBoxUiState.allItemsOfBox
        /*boxes = when (controlType) {
             ControlType.All -> items
            ControlType.Done -> items.filter {  }
         }*/
    }

    BaseCountScreen(itemCount = bookInBoxUiState.allItemsOfBox.size, onTabSelected = {
        controlType = it
    }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(bookInBoxUiState.allItemsOfBox) {
                ExpandedScannedItem(it.toExpandedScannedItems())
            }
        }
    }
}