package com.etag.stsyn.ui.screen.book_in.book_in_box

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.People
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
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.SuccessDialog
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun BookInBoxSaveScreen(
    bookInBoxViewModel: BookInBoxViewModel, modifier: Modifier = Modifier
) {
    val bookInBoxUiState by bookInBoxViewModel.boxUiState.collectAsState()
    val scannedItemList by bookInBoxViewModel.scannedItemsList.collectAsState()
    val user by bookInBoxViewModel.user.collectAsState(initial = LocalUser())
    val saveBookInBoxResponse by bookInBoxViewModel.saveBookInBoxResponse.collectAsState()
    var shouldShowRefreshIcon by remember { mutableStateOf(false) }

    LaunchedEffect(bookInBoxUiState.issuerUser) {
        shouldShowRefreshIcon = bookInBoxUiState.issuerUser != null
    }

    when (saveBookInBoxResponse) {
        is ApiResponse.Loading -> LoadingDialog(title = "Please wait while SMS is processing your request...",
            showDialog = true,
            onDismiss = { })

        is ApiResponse.Success -> {
            bookInBoxViewModel.apply {
                updateIsSavedStatus(true)
                updateSuccessDialogVisibility(true)
            }
        }

        is ApiResponse.ApiError -> WarningDialog(
            icon = CustomIcon.Vector(Icons.Default.Error),
            message = (saveBookInBoxResponse as ApiResponse.ApiError).message,
            showDialog = true,
            positiveButtonTitle = "try again",
            onPositiveButtonClick = bookInBoxViewModel::saveBookInBox
        )

        is ApiResponse.AuthorizationError -> bookInBoxViewModel.shouldShowAuthorizationFailedDialog(
            true
        )

        else -> {}
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