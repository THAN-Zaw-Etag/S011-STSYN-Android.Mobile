package com.etag.stsyn.ui.screen.book_in.book_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ControlType
import com.etag.stsyn.ui.components.ExpandedScannedItem
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.tzh.retrofit_module.data.mapper.toExpandedScannedItems
import com.tzh.retrofit_module.domain.model.bookIn.BookInItem
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.util.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun BookInCountScreen(
    viewModel: BookInViewModel,
    modifier: Modifier = Modifier
) {
    val scannedItemIdList by viewModel.scannedItemIdList.collectAsState()
    val bookInState by viewModel.bookInState.collectAsState()
    var bookInAllItems by remember { mutableStateOf(bookInState.allBookInItems) }
    var controlType by remember { mutableStateOf(ControlType.All) }

    LaunchedEffect(controlType) {
        withContext(Dispatchers.IO) {
            val items = bookInState.allBookInItems
            bookInAllItems = when (controlType) {
                ControlType.All -> items
                ControlType.Done -> items.filter { it.epc in scannedItemIdList}
                ControlType.Outstanding -> items.filter { it.epc !in scannedItemIdList }
            }
        }
    }

    BaseCountScreen(modifier = modifier, itemCount = bookInAllItems.size, onTabSelected = { control -> controlType = control}
    ) {
        LazyColumn(
            modifier = Modifier,
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(bookInAllItems) {
                key(it.epc) {
                    ExpandedScannedItem(
                        bookInItem = it.toExpandedScannedItems()
                    )
                }
            }
        }
    }
}