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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.ErrorText
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseScanScreen
import com.etag.stsyn.ui.states.rememberMutableDialogState
import com.etag.stsyn.ui.theme.errorColor

@Composable
fun CheckInOutScreen(
    onsiteCheckInOutViewModel: OnsiteCheckInOutViewModel, modifier: Modifier = Modifier
) {

    val rfidUiState by onsiteCheckInOutViewModel.rfidUiState.collectAsStateWithLifecycle()
    val onsiteCheckInOutUiState by onsiteCheckInOutViewModel.onSiteCheckInOutUiState.collectAsStateWithLifecycle()
    val scannedItemList by onsiteCheckInOutViewModel.scannedItemList.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val dialogState = rememberMutableDialogState(data = "")

    LaunchedEffect(onsiteCheckInOutUiState.shouldShowWarningDialog) {
        if (onsiteCheckInOutUiState.shouldShowWarningDialog) dialogState.showDialog("All items must be from same user")
    }

    LaunchedEffect(Unit) {
        onsiteCheckInOutViewModel.apply {
            updateScanType(BaseViewModel.ScanType.Multi)
            updateOnsiteScanType(OnsiteCheckInOutViewModel.OnSiteScanType.ITEMS)
        }
    }

    // show this dialog when reader error occurs
    WarningDialog(
        icon = CustomIcon.Vector(Icons.Default.Error),
        dialogState = dialogState,
        color = errorColor,
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
                        ScannedItem(id = "${it.serialNo} - ${it.itemLocation}",
                            isSwipeable = true,
                            name = it.description,
                            onSwipeToDismiss = { onsiteCheckInOutViewModel.removeScannedItem(it.epc) })
                    }
                }
            }
        }
    }
}