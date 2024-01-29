package com.etag.stsyn.ui.screen.book_out.book_out

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.tzh.retrofit_module.domain.model.bookOut.BookOutResponse
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
            Toast.makeText(
                context,
                "${(getAllBookOutItemsResponse as ApiResponse.Success<BookOutResponse>).data?.items!!.size} of items are fetched.",
                Toast.LENGTH_SHORT
            ).show()
        }

        is ApiResponse.ApiError -> WarningDialog(
            icon = CustomIcon.Vector(Icons.Default.Error),
            message = (getAllBookOutItemsResponse as ApiResponse.ApiError).message,
            showDialog = true,
            positiveButtonTitle = "Try again",
            onPositiveButtonClick = {}
        )

        is ApiResponse.AuthorizationError -> bookOutViewModel.updateAuthorizationFailedDialogVisibility(
            true
        )

        else -> {}
    }

    BaseScanScreen(
        scannedItemCount = rfidUiState.scannedItems.size,
        modifier = modifier,
        isScanning = rfidUiState.isScanning,
        onScan = { bookOutViewModel.toggle() },
        onClear = { rfidUiState.scannedItems.toMutableList().clear() }) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(bookOutUiState.scannedItems) {
                key(it) {
                    ScannedItem(
                        id = it.epc, name = it.description,
                        isSwipeable = true,
                        onSwipeToDismiss = {
                            //bookOutViewModel.removeItem(it)
                        }
                    )
                }
            }
        }
    }
}