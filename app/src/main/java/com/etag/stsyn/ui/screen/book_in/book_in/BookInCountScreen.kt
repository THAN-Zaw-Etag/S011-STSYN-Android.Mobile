package com.etag.stsyn.ui.screen.book_in.book_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
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
import com.tzh.retrofit_module.domain.model.bookIn.BookInItem
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun BookInCountScreen(
    viewModel: BookInViewModel,
    modifier: Modifier = Modifier
) {
    val scannedItems by viewModel.scannedBookInItems.collectAsState()
    val bookInItemsResponse by viewModel.bookInItemsResponse.collectAsState()

    var items by remember { mutableStateOf<List<BookInItem>>(emptyList()) }

    BaseCountScreen(itemCount = items.size, onTabSelected = { controlType ->
        when (controlType) {
            ControlType.All -> {
                if (bookInItemsResponse is ApiResponse.Success) {
                    items =
                        (bookInItemsResponse as ApiResponse.Success<BookInResponse>).data!!.items
                }
            }

            ControlType.Done -> {
                items = scannedItems.map { it!! }
            }

            ControlType.Outstanding -> {}

            else -> {}
        }

    }
    ) {
        LazyColumn(
            modifier = Modifier,
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(scannedItems) {
                ExpandedScannedItem(
                    bookInItem = it
                )
            }
        }
    }
}