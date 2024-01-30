package com.etag.stsyn.ui.screen.book_out.book_out

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.TrackChanges
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
import com.tzh.retrofit_module.data.settings.AppConfigModel
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun BookOutSaveScreen(
    bookOutViewModel: BookOutViewModel, modifier: Modifier = Modifier
) {
    var location by remember { mutableStateOf("") }
    val settings by bookOutViewModel.settings.collectAsState(initial = AppConfigModel())
    val bookOutUiState by bookOutViewModel.bookOutUiState.collectAsState()
    val saveBookOutBoxesResponse by bookOutViewModel.saveBookOutBoxesResponse.collectAsState()

    when (saveBookOutBoxesResponse) {
        is ApiResponse.Loading -> {
            LoadingDialog(
                title = "Please wait while SMS is processing your request...\n",
                showDialog = true,
                onDismiss = {})
        }
        
        is ApiResponse.Success -> SuccessDialog(
            showDialog = true,
            title = "SUCCESS!",
            onDoneClick = { /*TODO*/ })
        
        is ApiResponse.ApiError -> WarningDialog(
            icon = CustomIcon.Vector(Icons.Default.Error),
            message = (saveBookOutBoxesResponse as ApiResponse.ApiError).message,
            showDialog = true,
            positiveButtonTitle = "try again",
            onPositiveButtonClick = bookOutViewModel::saveBookOutItems
        )

        is ApiResponse.AuthorizationError -> bookOutViewModel.updateAuthorizationFailedDialogVisibility(true)
        else -> {}
    }

    LaunchedEffect(bookOutUiState) {
        if (bookOutUiState.scannedItems.isEmpty()) bookOutViewModel.updateBookOutErrorMessage("Please read an item first.")
        else bookOutViewModel.updateBookOutErrorMessage(null)
    }

    BaseSaveScreen(
        isError = bookOutUiState.errorMessage != null && bookOutUiState.scannedItems.isEmpty(),
        errorMessage = bookOutUiState.errorMessage ?: "",
        onSave = bookOutViewModel::saveBookOutItems
    ) {

        Column(
            modifier = modifier.fillMaxSize()
        ) {
            SaveItemLayout(
                icon = Icons.Default.Person, itemTitle = "User"
            ) { Text(text = "Admin - 123S") }

            SaveItemLayout(icon = Icons.Outlined.TrackChanges, itemTitle = "Purpose") {
                DropDown(items = Purpose.entries.map { it.toString() },
                    defaultValue = "Choose a purpose",
                    onSelected = { item ->
                        bookOutViewModel.updatePurpose(item)
                    })
            }

            if (settings.needLocation) {
                SaveItemLayout(icon = Icons.Default.AddLocation, itemTitle = "Location") {
                    STSYNTExtField(
                        value = location,
                        label = {},
                        onValueChange = {
                            location = it
                            bookOutViewModel.updateLocation(location)
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