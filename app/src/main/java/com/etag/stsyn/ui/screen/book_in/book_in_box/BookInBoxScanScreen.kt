@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.book_in.book_in_box

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.ui.screen.base.BaseBoxScreen
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem

@Composable
fun BookInBoxScanScreen(
    bookInBoxViewModel: BookInBoxViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookInBoxViewModel.rfidUiState.collectAsState()
    val bookInBoxUiState by bookInBoxViewModel.boxUiState.collectAsState()
    var scannedBox by remember { mutableStateOf(BoxItem()) }
    var boxes by remember { mutableStateOf<List<BoxItem>>(emptyList()) }
    val scannedItemList by bookInBoxViewModel.scannedItemsList.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(bookInBoxUiState) {
        scannedBox = bookInBoxUiState.scannedBox
        boxes = bookInBoxUiState.allBoxes

        if (bookInBoxUiState.itemsCountNotInBox != 0)
            Toast.makeText(
                context,
                "${bookInBoxUiState.itemsCountNotInBox} items in this box be booked out",
                Toast.LENGTH_SHORT
            ).show()
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