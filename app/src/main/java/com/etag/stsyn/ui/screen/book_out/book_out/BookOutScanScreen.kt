package com.etag.stsyn.ui.screen.book_out.book_out

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseScanScreen
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun BookOutScanScreen(
    bookOutViewModel: BookOutViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookOutViewModel.rfidUiState.collectAsState()
    val listState = rememberLazyListState()
    val bookOutUiState by bookOutViewModel.bookOutUiState.collectAsState()
    val getAllBookOutItemsResponse by bookOutViewModel.getAllBookOutItemResponse.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(rfidUiState.scannedItems) {
        if (rfidUiState.scannedItems.size > 1) listState.animateScrollToItem(rfidUiState.scannedItems.size - 1)
    }

    when (getAllBookOutItemsResponse) {
        is ApiResponse.Loading -> LoadingDialog(
            title = "Loading book out items...",
            showDialog = true,
            onDismiss = { /*TODO*/ })

        is ApiResponse.Success -> {
            // TODO do something here...
        }

        is ApiResponse.ApiError -> WarningDialog(
            icon = CustomIcon.Vector(Icons.Default.Error),
            message = (getAllBookOutItemsResponse as ApiResponse.ApiError).message,
            showDialog = true,
            positiveButtonTitle = "Try again",
            onPositiveButtonClick = {}
        )

        is ApiResponse.AuthorizationError -> bookOutViewModel.shouldShowAuthorizationFailedDialog(
            true
        )

        else -> {}
    }

    BaseScanScreen(
        scannedItemCount = bookOutUiState.scannedItems.size,
        modifier = modifier,
        isScanning = rfidUiState.isScanning,
        onScan = bookOutViewModel::toggle,
        onClear = bookOutViewModel::clearAllScannedItems) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(bookOutUiState.scannedItems) {index,item ->
                key(item.epc) {
                    ScannedItem(
                        id = item.epc, name = item.description,
                        isSwipeable = true,
                        onSwipeToDismiss = {
                            bookOutViewModel.removeScannedItem(index)
                        }
                    )
                }
            }
        }
    }
}