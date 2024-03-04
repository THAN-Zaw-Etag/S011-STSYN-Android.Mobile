@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.book_out.book_out_box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.components.ControlType
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.components.ScreenWithBottomSheet
import com.etag.stsyn.ui.components.listItemsIndexed
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.etag.stsyn.ui.screen.bottomsheet.BoxDetailScreen
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import kotlinx.coroutines.launch

@Composable
fun BookOutBoxCountScreen(
    bookOutBoxViewModel: BookOutBoxViewModel,
    modifier: Modifier = Modifier
) {
    val boxUiState by bookOutBoxViewModel.boxUiState.collectAsStateWithLifecycle()
    var controlType by remember { mutableStateOf(ControlType.All) }
    var boxes by remember { mutableStateOf(boxUiState.allBoxes) }
    var boxItem by remember { mutableStateOf(BoxItem()) }
    val scannedItemList by bookOutBoxViewModel.scannedItemList.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(controlType) {
        boxes = when (controlType) {
            ControlType.All -> boxUiState.allBoxes
            ControlType.Done -> boxUiState.allBoxes.filter { it.epc in scannedItemList }
                .toMutableList()

            ControlType.Outstanding -> boxUiState.allBoxes.filter { it.epc !in scannedItemList }
                .toMutableList()
        }
    }

    ScreenWithBottomSheet(
        show = showBottomSheet,
        onDismiss = { showBottomSheet = false },
        sheetContent = {
            BoxDetailScreen(boxItem = boxItem)
        }
    ) {
        BaseCountScreen(
            itemCount = boxes.size,
            modifier = modifier,
            onTabSelected = {
                controlType = it
            }
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                listItemsIndexed(boxes) { index, item ->
                    val currentIndex = index + 1
                    key(item.epc) {
                        ScannedItem(
                            id = "${item.serialNo} - ${item.itemLocation}",
                            name = "Box 01 item ${if (currentIndex < 10) "0${currentIndex}" else currentIndex}",
                            showTrailingIcon = true,
                            onItemClick = {
                                scope.launch { sheetState.expand() }
                                boxItem = item
                                showBottomSheet = true
                            }
                        )
                    }
                }
            }
        }
    }
}