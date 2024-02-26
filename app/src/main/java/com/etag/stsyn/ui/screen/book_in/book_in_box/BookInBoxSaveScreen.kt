package com.etag.stsyn.ui.screen.book_in.book_in_box

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun BookInBoxSaveScreen(
    bookInBoxViewModel: BookInBoxViewModel, modifier: Modifier = Modifier
) {
    val bookInBoxUiState by bookInBoxViewModel.boxUiState.collectAsStateWithLifecycle()
    val scannedItemList by bookInBoxViewModel.scannedItemsList.collectAsStateWithLifecycle()
    val user by bookInBoxViewModel.userFlow.collectAsStateWithLifecycle(LocalUser())
    val saveBookInBoxResponse by bookInBoxViewModel.saveBookInBoxResponse.collectAsStateWithLifecycle()
    var shouldShowRefreshIcon by remember { mutableStateOf(false) }

    var shouldShowWarningDialog by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf("")
    }

    var attemptCount by remember { mutableStateOf(0) }

    LaunchedEffect(bookInBoxUiState.issuerUser) {
        shouldShowRefreshIcon = bookInBoxUiState.issuerUser != null
    }

    when (saveBookInBoxResponse) {
        is ApiResponse.Loading -> {
            shouldShowWarningDialog = false
            LoadingDialog(title = "Please wait while SMS is processing your request...",
                showDialog = true,
                onDismiss = { })
        }

        is ApiResponse.Success -> {
            shouldShowWarningDialog = false
            bookInBoxViewModel.apply {
                updateIsSavedStatus(true)
                updateSuccessDialogVisibility(true)
            }
        }

        is ApiResponse.ApiError -> {
            shouldShowWarningDialog = true
            errorMessage = (saveBookInBoxResponse as ApiResponse.ApiError).message

        }

        is ApiResponse.AuthorizationError -> {
            shouldShowWarningDialog = false
            bookInBoxViewModel.shouldShowAuthorizationFailedDialog(
                true
            )
        }

        else -> {
            shouldShowWarningDialog = false
        }
    }


    if (shouldShowWarningDialog) {
        WarningDialog(
            attemptAccount = attemptCount,
            message = errorMessage,
            onProcess = {
                attemptCount++
                bookInBoxViewModel.saveBookInBox()
            }, onDismiss = { attemptCount = 0})
    }
    BaseSaveScreen(
        isError = scannedItemList.isEmpty(),
        isUsCase = bookInBoxUiState.issuerUser == null,
        errorMessage = if (bookInBoxUiState.scannedBox.epc.isEmpty()) "Please read a box first" else "Please read an item first",
        modifier = modifier,
        onScan = {
            bookInBoxViewModel.apply {
                enableScan()
                updateScanType(BaseViewModel.ScanType.Single)
                toggle()
            }
        },
        onSave = bookInBoxViewModel::saveBookInBox
    ) {

        SaveItemLayout(
            icon = Icons.Default.Person,
            itemTitle = "User",
        ) {
            Text(text = "${user.name}-${user.userId}")
        }

        if (bookInBoxUiState.isUsCase) { //TODO replace true with bookInBoxUiState.isUsCase
            SaveItemLayout(icon = Icons.Default.People,
                itemTitle = "Buddy",
                showRefreshIcon = shouldShowRefreshIcon,
                onRefresh = {
                    /*bookInBoxViewModel.apply {
                        enableScan()
                        updateScanType(BaseViewModel.ScanType.Single)
                        toggle()
                    }*/
                }) {
                Text(text = "${bookInBoxUiState.issuerUser?.userName ?: ""} - ${bookInBoxUiState.issuerUser?.userId ?: ""}")
            }
        }
    }
}