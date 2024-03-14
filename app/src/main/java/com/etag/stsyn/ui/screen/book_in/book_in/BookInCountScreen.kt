@file:OptIn(ExperimentalFoundationApi::class)

package com.etag.stsyn.ui.screen.book_in.book_in

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.components.ControlType
import com.etag.stsyn.ui.components.ExpandedScannedItem
import com.etag.stsyn.ui.components.listItems
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.tzh.retrofit_module.data.mapper.toExpandedScannedItems


@Composable
fun BookInCountScreen(
    viewModel: BookInViewModel,
    modifier: Modifier = Modifier
) {
    val scannedItemIdList by viewModel.scannedItemIdList.collectAsStateWithLifecycle()
    val bookInState by viewModel.bookInState.collectAsStateWithLifecycle()
    var bookInAllItems by remember { mutableStateOf(bookInState.allBookInItems) }
    var controlType by remember { mutableStateOf(ControlType.All) }

    LaunchedEffect(controlType, bookInState.allBookInItems) {
        val items = bookInState.allBookInItems
        bookInAllItems = when (controlType) {
            ControlType.All -> items
            ControlType.Done -> items.filter { it.epc in scannedItemIdList }
            ControlType.Outstanding -> items.filter { it.epc !in scannedItemIdList }
        }
    }

    BaseCountScreen(
        modifier = modifier,
        itemCount = bookInAllItems.size,
        onTabSelected = { control -> controlType = control }
    ) {
        LazyColumn(
            modifier = Modifier,
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listItems(bookInAllItems.map { it.toExpandedScannedItems() }) {
                key(it.code) {
                    ExpandedScannedItem(
                        bookInItem = it
                    )
                }
            }
        }
    }
}