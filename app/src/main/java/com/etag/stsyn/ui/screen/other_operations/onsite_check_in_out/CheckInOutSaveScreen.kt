package com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.UiEvent
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun CheckInOutSaveScreen(
    onsiteCheckInOutViewModel: OnsiteCheckInOutViewModel, modifier: Modifier = Modifier
) {
    val TAG = "CheckInOutSaveScreen" //TODO might delete later

    val hasScannedItems by remember { mutableStateOf(false) }
    val scannedItemList by onsiteCheckInOutViewModel.scannedItemList.collectAsState()
    val onsiteCheckInOutUiState by onsiteCheckInOutViewModel.onSiteCheckInOutUiState.collectAsState()
    val saveOnsiteCheckInOutResponse by onsiteCheckInOutViewModel.saveOnSiteCheckInOutResponse.collectAsState()
    val user by onsiteCheckInOutViewModel.userFlow.collectAsState(initial = LocalUser())
    var showErrorDialog by remember { mutableStateOf(false) }
    
    when(saveOnsiteCheckInOutResponse) {
        is ApiResponse.Loading -> LoadingDialog(
            title = "Please wait while SMS is processing your request...\n",
            showDialog = true,
            onDismiss = {})

        is ApiResponse.Success -> {
            onsiteCheckInOutViewModel.updateSuccessDialogVisibility(true)
        }

        is ApiResponse.ApiError -> {

        }

        is ApiResponse.AuthorizationError -> onsiteCheckInOutViewModel.shouldShowAuthorizationFailedDialog(true)
        else -> {}
    }

    LaunchedEffect(Unit) {
        onsiteCheckInOutViewModel.updateOnsiteScanType(OnsiteCheckInOutViewModel.OnSiteScanType.RECEIVER)
        onsiteCheckInOutViewModel.apply {
            enableScan()
            updateScanType(BaseViewModel.ScanType.Single)
        }
    }

    LaunchedEffect(scannedItemList) {
        if (scannedItemList.isEmpty()) onsiteCheckInOutViewModel.updateOnsiteCheckInOutErrorMessage(
            "Please read an item first!"
        )
        else onsiteCheckInOutViewModel.updateOnsiteCheckInOutErrorMessage(null)
    }

    BaseSaveScreen(
        isError = scannedItemList.isEmpty(),
        modifier = modifier,
        errorMessage = onsiteCheckInOutUiState.errorMessage ?: "",
        isUsCase = onsiteCheckInOutUiState.receiver == null,
        onScan = {
            onsiteCheckInOutViewModel.apply {
                updateScanType(BaseViewModel.ScanType.Single)
                enableScan()
                toggle()
            }
        },
        onSave = onsiteCheckInOutViewModel::saveOnsiteCheckInOut
    ) {
        SaveItemLayout(
            icon = Icons.Default.Person,
            itemTitle = "Issuer",
        ) {
            Text(text = "${user.name}-${user.userId}")
        }
        SaveItemLayout(
            icon = Icons.Default.Person, itemTitle = "Receiver", showRefreshIcon = hasScannedItems
        ) {
            Text(text = "${onsiteCheckInOutUiState.receiver?.userName ?: ""}-${onsiteCheckInOutUiState.receiver?.userId ?: ""}")
        }
    }
}
