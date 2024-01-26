@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.book_in.book_in_box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ControlType
import com.etag.stsyn.ui.components.DetailBottomSheetScaffold
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.etag.stsyn.ui.screen.bottomsheet.BoxDetailScreen
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import kotlinx.coroutines.launch

@Composable
fun BookInBoxCountScreen(
    bookInBoxViewModel: BookInBoxViewModel, modifier: Modifier = Modifier
) {
    val bookInBoxUiState by bookInBoxViewModel.bookInBoxUiState.collectAsState()
    var boxes by remember { mutableStateOf(bookInBoxUiState.allItemsOfBox) }
    var controlType by remember { mutableStateOf(ControlType.All) }
    val scannedItemList by bookInBoxViewModel.scannedItemsList.collectAsState()
    var boxItem by remember { mutableStateOf(BoxItem()) }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(skipPartiallyExpanded = true, skipHiddenState = false)
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(controlType) {
        val items = bookInBoxUiState.allItemsOfBox
        boxes = when (controlType) {
            ControlType.All -> items
            ControlType.Done -> items.filter { it.epc in scannedItemList }.toMutableList()
            ControlType.Outstanding -> items.filter { it.epc !in scannedItemList }.toMutableList()
        }
    }

    DetailBottomSheetScaffold(
        state = scaffoldState,
        sheetContent = { BoxDetailScreen(boxItem = boxItem) }) {
        BaseCountScreen(
            itemCount = boxes.size,
            modifier = modifier,
            onTabSelected = {
                controlType = it
            }) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                itemsIndexed(boxes) { index, item ->
                    ScannedItem(
                        id = "${item.serialNo} - ${item.itemLocation}",
                        name = "Box 01 item ${if (index < 10) "0$index" else index}",
                        showTrailingIcon = true,
                        onItemClick = {
                            boxItem = item
                            if (scaffoldState.bottomSheetState.isVisible) coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                            else coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                        })
                }
            }
        }
    }
}
