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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.etag.stsyn.enums.Purpose
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.DropDown
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.STSYNTExtField
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.SuccessDialog
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.etag.stsyn.ui.theme.Purple80
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun BookOutBoxSaveScreen(
    bookOutBoxViewModel: BookOutBoxViewModel,
    modifier: Modifier = Modifier
) {
    val user by bookOutBoxViewModel.user.collectAsState(initial = LocalUser())
    val boxUiState by bookOutBoxViewModel.boxUiState.collectAsState()
    val bookOutBoxUiState by bookOutBoxViewModel.bookOutBoxUiState.collectAsState()
    val scannedItemList by bookOutBoxViewModel.scannedItemList.collectAsState()
    val needLocation by bookOutBoxViewModel.needLocation.collectAsState()
    val saveBookOutBoxesResponse by bookOutBoxViewModel.saveBookOutBoxResponse.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf("") }

    LaunchedEffect(bookOutBoxUiState.errorMessage) {
        showError = bookOutBoxUiState.errorMessage == null
    }

    LaunchedEffect(boxUiState.scannedBox,scannedItemList,Unit) {
        if (boxUiState.scannedBox.epc.isEmpty()) bookOutBoxViewModel.updateBookOutBoxErrorMessage("Please scan a box tag first!")
        else if (scannedItemList.isEmpty()) bookOutBoxViewModel.updateBookOutBoxErrorMessage("Pleas read an item first!")
        else bookOutBoxViewModel.updateBookOutBoxErrorMessage(null)
    }

    when (saveBookOutBoxesResponse) {
        is ApiResponse.Loading -> {
            showError = false
            LoadingDialog(title = "Please wait while SMS is processing your request...",
                showDialog = true,
                onDismiss = { })
        }
        is ApiResponse.Success -> {
            showError = false
            bookOutBoxViewModel.updateIsSavedStatus(true)
            bookOutBoxViewModel.updateSuccessDialogVisibility(true)
        }
        is ApiResponse.ApiError -> showError = true
        is ApiResponse.AuthorizationError -> bookOutBoxViewModel.shouldShowAuthorizationFailedDialog(true)
        else -> showError = false
    }


    WarningDialog(
        icon = CustomIcon.Vector(Icons.Default.Error),
        message = bookOutBoxUiState.errorMessage ?: "",
        showDialog = showError,
        positiveButtonTitle = "try again",
        onPositiveButtonClick = {
            showError = false
        }
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
                DropDown(
                    items = Purpose.entries.map { it.toString() },
                    defaultValue = "Choose a purpose",
                    onSelected = { item ->
                        bookOutBoxViewModel.updatePurpose(item)
                    }
                )
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