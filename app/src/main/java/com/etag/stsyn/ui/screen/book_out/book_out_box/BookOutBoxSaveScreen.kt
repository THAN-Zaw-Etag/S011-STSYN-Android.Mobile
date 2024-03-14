package com.etag.stsyn.ui.screen.book_out.book_out_box

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.enums.Purpose
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.DropDown
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.STSYNTExtField
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.etag.stsyn.ui.states.rememberMutableDialogState
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.util.ErrorMessages
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun BookOutBoxSaveScreen(
    bookOutBoxViewModel: BookOutBoxViewModel, modifier: Modifier = Modifier
) {
    val user by bookOutBoxViewModel.user.collectAsStateWithLifecycle(LocalUser())
    val boxUiState by bookOutBoxViewModel.boxUiState.collectAsStateWithLifecycle()
    val bookOutBoxUiState by bookOutBoxViewModel.bookOutBoxUiState.collectAsStateWithLifecycle()
    val scannedItemList by bookOutBoxViewModel.scannedItemList.collectAsStateWithLifecycle()
    val needLocation by bookOutBoxViewModel.needLocation.collectAsStateWithLifecycle()
    val saveBookOutBoxesResponse by bookOutBoxViewModel.saveBookOutBoxResponse.collectAsStateWithLifecycle()
    var location by remember { mutableStateOf("") }
    val dialogState = rememberMutableDialogState(data = "")

    LaunchedEffect(boxUiState.scannedBox, bookOutBoxUiState, scannedItemList, Unit) {
        val scannedBox = boxUiState.scannedBox
        val isEmptyBoxEpc = scannedBox.epc.isEmpty()
        val isItemListEmpty = scannedItemList.isEmpty()
        val isPurposeEmpty = bookOutBoxUiState.purpose.isEmpty()

        // Update error message based on conditions
        if (isEmptyBoxEpc) {
            bookOutBoxViewModel.updateBookOutBoxErrorMessage(ErrorMessages.READ_A_BOX_FIRST)
            return@LaunchedEffect
        }
        if (isItemListEmpty) {
            bookOutBoxViewModel.updateBookOutBoxErrorMessage(ErrorMessages.READ_AN_ITEM_FIRST)
            return@LaunchedEffect
        }

        if (isPurposeEmpty) {
            bookOutBoxViewModel.updateBookOutBoxErrorMessage(ErrorMessages.CHOOSE_PURPOSE)
            return@LaunchedEffect
        }
        // No error, clear the error message
        bookOutBoxViewModel.updateBookOutBoxErrorMessage(null)
    }

    when (saveBookOutBoxesResponse) {
        is ApiResponse.Loading -> {
            LoadingDialog(
                title = "Please wait while SMS is processing your request...",
                showDialog = true,
                onDismiss = { }
            )
        }

        is ApiResponse.Success -> {
            bookOutBoxViewModel.updateIsSavedStatus(true)
        }

        is ApiResponse.ApiError -> dialogState.showDialog((saveBookOutBoxesResponse as ApiResponse.ApiError).message)
        is ApiResponse.AuthorizationError -> bookOutBoxViewModel.shouldShowAuthorizationFailedDialog(
            true
        )

        else -> {}
    }


    WarningDialog(
        icon = CustomIcon.Vector(Icons.Default.Error),
        dialogState = dialogState,
        positiveButtonTitle = "try again",
        onPositiveButtonClick = bookOutBoxViewModel::saveBookOutBoxItems
    )

    BaseSaveScreen(
        isError = bookOutBoxUiState.errorMessage != null,
        errorMessage = bookOutBoxUiState.errorMessage ?: "",
        onSave = bookOutBoxViewModel::saveBookOutBoxItems
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
                Text(text = "${user.name} - ${user.nric}")
            }
            SaveItemLayout(icon = Icons.Default.TrackChanges, itemTitle = "Purpose") {
                DropDown(items = Purpose.entries.map { it.toString() },
                    defaultValue = if (bookOutBoxUiState.purpose.isEmpty()) "Choose a purpose" else bookOutBoxUiState.purpose,
                    onSelected = { item ->
                        bookOutBoxViewModel.updatePurpose(item)
                    })
            }
            if (needLocation) {
                SaveItemLayout(icon = Icons.Default.AddLocation, itemTitle = "Location") {
                    STSYNTExtField(
                        value = location,
                        label = {},
                        onValueChange = {
                            location = it
                            bookOutBoxViewModel.updateLocation(location)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp, color = Purple80, shape = RoundedCornerShape(5.dp)
                            ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                    )
                }
            }
        }
    }
}