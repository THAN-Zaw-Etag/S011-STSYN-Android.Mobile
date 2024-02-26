package com.etag.stsyn.ui.screen.book_out.book_out

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
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
import com.etag.stsyn.ui.components.DropDown
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.STSYNTExtField
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.etag.stsyn.ui.theme.Purple80
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.data.settings.AppConfigModel
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun BookOutSaveScreen(
    bookOutViewModel: BookOutViewModel, modifier: Modifier = Modifier
) {
    val TAG = "BookOutSaveScreen"
    var location by remember { mutableStateOf("") }
    val settings by bookOutViewModel.settings.collectAsState(initial = AppConfigModel())
    val bookOutUiState by bookOutViewModel.bookOutUiState.collectAsState()
    val user by bookOutViewModel.user.collectAsState(initial = LocalUser())
    val saveBookOutBoxesResponse by bookOutViewModel.saveBookOutBoxesResponse.collectAsState()
    var isError by remember { mutableStateOf(false) }
    var shouldShowWarningDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var attemptCount by remember { mutableStateOf(0) }

    LaunchedEffect(bookOutUiState.errorMessage) {
        isError = bookOutUiState.errorMessage != null && bookOutUiState.errorMessage!!.isNotEmpty()
    }

    when (saveBookOutBoxesResponse) {
        is ApiResponse.Loading -> {
            shouldShowWarningDialog = false
            LoadingDialog(
                title = "Please wait while SMS is processing your request...\n",
                showDialog = true,
                onDismiss = {})
        }

        is ApiResponse.Success -> {
            shouldShowWarningDialog = false
        }

        is ApiResponse.ApiError -> {
            shouldShowWarningDialog = true
            errorMessage = (saveBookOutBoxesResponse as ApiResponse.ApiError).message
        }

        is ApiResponse.AuthorizationError -> {
            shouldShowWarningDialog = false
            bookOutViewModel.shouldShowAuthorizationFailedDialog(true)
        }

        else -> {
            shouldShowWarningDialog = false
            Log.d("BookOutSaveScreen", "Default")
        }
    }


    if (shouldShowWarningDialog) {
        WarningDialog(
            attemptAccount = attemptCount,
            message = errorMessage,
            onProcess = {
                attemptCount++
                bookOutViewModel.saveBookOutItems()
            }, onDismiss = { attemptCount = 0})
    }

    LaunchedEffect(bookOutUiState.scannedItems, bookOutUiState.location) {
        if (bookOutUiState.location.isNotEmpty()) bookOutViewModel.setBookOutErrorMessage(null)
        if (bookOutUiState.scannedItems.isEmpty()) bookOutViewModel.setBookOutErrorMessage("Please read an item first.")
        else bookOutViewModel.setBookOutErrorMessage(null)
    }

    BaseSaveScreen(
        isError = isError,
        errorMessage = bookOutUiState.errorMessage ?: "",
        onSave = bookOutViewModel::saveBookOutItems,
    ) {

        Column(
            modifier = modifier.fillMaxSize()
        ) {
            SaveItemLayout(
                icon = Icons.Default.Person, itemTitle = "User"
            ) { Text(text = "${user.name}-${user.userId}") }

            SaveItemLayout(icon = Icons.Outlined.TrackChanges, itemTitle = "Purpose") {
                DropDown(items = Purpose.entries.map { it.toString() },
                    defaultValue = "Choose a purpose",
                    onSelected = { item ->
                        bookOutViewModel.setPurpose(item)
                    })
            }

            if (settings.needLocation) {
                SaveItemLayout(icon = Icons.Default.AddLocation, itemTitle = "Location") {
                    STSYNTExtField(
                        value = location,
                        label = {},
                        onValueChange = {
                            location = it
                            bookOutViewModel.setLocation(location)
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