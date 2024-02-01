package com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.ErrorText
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseScanScreen
import com.etag.stsyn.ui.theme.errorColor
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun CheckInOutScreen(
    onsiteCheckInOutViewModel: OnsiteCheckInOutViewModel, modifier: Modifier = Modifier
) {
    val TAG = "CheckInOutScreen" //TODO might delete later

    val rfidUiState by onsiteCheckInOutViewModel.rfidUiState.collectAsState()
    val getItemsForOnsiteResponse by onsiteCheckInOutViewModel.getAllItemsForOnsiteResponse.collectAsState()
    val onsiteCheckInOutUiState by onsiteCheckInOutViewModel.onSiteCheckInOutUiState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var attemptCount by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf("") }
    val scannedItemList by onsiteCheckInOutViewModel.scannedItemList.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        onsiteCheckInOutViewModel.apply{
            updateScanType(BaseViewModel.ScanType.Multi)
            updateOnsiteScanType(OnsiteCheckInOutViewModel.OnSiteScanType.ITEMS)
        }
    }

    when (getItemsForOnsiteResponse) {
        is ApiResponse.Loading -> LoadingDialog(title = "Loading...",
            showDialog = true,
            onDismiss = { /*TODO*/ })

        is ApiResponse.Success -> {
            showErrorDialog = false
        }

        is ApiResponse.ApiError -> {
            attemptCount++
            showErrorDialog = true
            errorMessage = (getItemsForOnsiteResponse as ApiResponse.ApiError).message
        }

        is ApiResponse.AuthorizationError -> {
            showErrorDialog = false
            onsiteCheckInOutViewModel.shouldShowAuthorizationFailedDialog(true)
        }

        else -> showErrorDialog = false
    }

    WarningDialog(icon = CustomIcon.Vector(Icons.Default.Error),
        message = if (attemptCount >= 3) "You've tried many times. Check your connection and try again." else errorMessage,
        showDialog = showErrorDialog,
        positiveButtonTitle = "try again",
        onPositiveButtonClick = {
            if (attemptCount >= 3) {
                attemptCount = 0
                // handle something
            } else onsiteCheckInOutViewModel.getAllItemsForOnsite()
        })

    WarningDialog(icon = CustomIcon.Vector(Icons.Default.Error),
        message = "All items must be from same user",
        color = errorColor,
        showDialog = onsiteCheckInOutUiState.shouldShowWarningDialog,
        positiveButtonTitle = "Ok",
        onPositiveButtonClick = { onsiteCheckInOutViewModel.updateWarningDialogVisibility(false) })

    LaunchedEffect(scannedItemList) {
        if (scannedItemList.size > 1) listState.animateScrollToItem(scannedItemList.size - 1)
    }

    Column(modifier = modifier.fillMaxSize()) {
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = "Note: Issuer and receiver must be from the same flight"
        )
        BaseScanScreen(scannedItemCount = scannedItemList.size,
            onScan = { onsiteCheckInOutViewModel.toggle() },
            isScanning = rfidUiState.isScanning,
            onClear = { onsiteCheckInOutViewModel.removeAllScannedItems() }) {
            LazyColumn(
                modifier = Modifier,
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(onsiteCheckInOutUiState.allItemsForOnsite.filter { it.epc in scannedItemList }) {
                    key(it.epc) {
                        ScannedItem(id = "${it.id} - ${it.partNo}",
                            isSwipeable = true,
                            name = it.description,
                            onSwipeToDismiss = { onsiteCheckInOutViewModel.removeScannedItem(it.epc) })
                    }
                }
            }
        }
    }
}