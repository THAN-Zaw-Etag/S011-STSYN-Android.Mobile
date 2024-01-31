@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.book_in.book_in_box

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.ui.screen.base.BaseBoxScreen
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.bookIn.SelectBoxForBookInResponse
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun BookInBoxScanScreen(
    bookInBoxViewModel: BookInBoxViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookInBoxViewModel.rfidUiState.collectAsState()
    val getAllBoxesResponse by bookInBoxViewModel.boxItemsForBookInResponse.collectAsState()
    val allItemsOfBoxResponse by bookInBoxViewModel.getAllItemsOfBox.collectAsState()
    val bookInBoxUiState by bookInBoxViewModel.boxUiState.collectAsState()
    var scannedBox by remember { mutableStateOf(BoxItem()) }
    var boxes by remember { mutableStateOf<List<BoxItem>>(emptyList()) }
    val scannedItemList by bookInBoxViewModel.scannedItemsList.collectAsState()

    LaunchedEffect(bookInBoxUiState) {
        scannedBox = bookInBoxUiState.scannedBox
    }

    when (getAllBoxesResponse) {
        is ApiResponse.Loading -> bookInBoxViewModel.toggleLoadingVisibility(true)
        is ApiResponse.Success -> {
            bookInBoxViewModel.toggleLoadingVisibility(false)
            boxes =
                (getAllBoxesResponse as ApiResponse.Success<SelectBoxForBookInResponse>).data!!.items
        }

        is ApiResponse.ApiError -> {
            bookInBoxViewModel.toggleLoadingVisibility(false)
            bookInBoxViewModel.updateErrorMessage((getAllBoxesResponse as ApiResponse.ApiError).message)
        }

        else -> {}
    }

    BaseBoxScreen(
        bookItems = bookInBoxUiState.allItemsOfBox,
        scannedItemList = scannedItemList,
        boxes = boxes,
        onReset = bookInBoxViewModel::resetScannedItems,
        onScan = {
            bookInBoxViewModel.apply {
                updateScanType(BaseViewModel.ScanType.Single)
                updateBookInBoxScanStatus(if (bookInBoxUiState.scannedBox.epc.isEmpty()) BoxScanType.BOX else BoxScanType.ITEMS)
                toggle()
            }
        },
        onRefresh = { bookInBoxViewModel.refreshScannedBox() },
        isScanning = rfidUiState.isScanning,
        scannedBox = scannedBox,
        modifier = modifier,
        checked = bookInBoxUiState.isChecked,
        onCheckChange = bookInBoxViewModel::toggleVisualCheck,
        showBoxBookOutButton = true,
        boxOutTitle = "Box booked out (${boxes.size})",
    )
}