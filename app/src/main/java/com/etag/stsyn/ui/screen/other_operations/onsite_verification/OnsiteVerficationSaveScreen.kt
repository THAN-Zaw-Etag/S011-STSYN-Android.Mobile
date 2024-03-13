package com.etag.stsyn.ui.screen.other_operations.onsite_verification

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.etag.stsyn.util.ErrorMessages
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun OnsiteVerificationSaveScreen(
    onsiteVerificationViewModel: OnsiteVerificationViewModel,
    modifier : Modifier = Modifier
) {
    val saveOnSiteVerification by onsiteVerificationViewModel.saveOnSiteVerification.collectAsState()
    val scannedItems by onsiteVerificationViewModel.totalScannedItems.collectAsState()
    var shouldShowWarningDialog by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf("")
    }

    var attemptCount by remember { mutableIntStateOf(0) }

    when (saveOnSiteVerification) {
        is ApiResponse.Loading -> {
            shouldShowWarningDialog = false
            LoadingDialog(title = "Please wait while SMS is processing your request...",
                showDialog = true,
                onDismiss = { })
        }

        is ApiResponse.Success -> {
            shouldShowWarningDialog = false
            onsiteVerificationViewModel.updateSuccessDialogVisibility(true)
        }

        is ApiResponse.ApiError -> {
            shouldShowWarningDialog = true
            errorMessage = (saveOnSiteVerification as ApiResponse.ApiError).message
        }

        is ApiResponse.AuthorizationError -> {
            shouldShowWarningDialog = false
        }

        ApiResponse.Default -> {
            shouldShowWarningDialog = false
        }
    }

    if (shouldShowWarningDialog) {
        WarningDialog(
            attemptAccount = attemptCount,
            message = errorMessage,
            onProcess = {
                attemptCount++
                onsiteVerificationViewModel.saveOnSiteVerification()
            }, onDismiss = { attemptCount = 0 })
    }

    BaseSaveScreen(
        modifier = modifier,
        isError = scannedItems.isEmpty(),
        errorMessage = if (scannedItems.isEmpty()) ErrorMessages.READ_A_BOX_FIRST else ErrorMessages.READ_AN_ITEM_FIRST,
        onSave = {
            onsiteVerificationViewModel.saveOnSiteVerification()
        }) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
            Text(text = "Admin - 123S")
        }
    }
}