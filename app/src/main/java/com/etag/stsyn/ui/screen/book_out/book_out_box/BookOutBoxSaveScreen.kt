package com.etag.stsyn.ui.screen.book_out.book_out_box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.enums.Purpose
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.DropDown
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.tzh.retrofit_module.data.model.LocalUser

@Composable
fun BookOutBoxSaveScreen(
    bookOutBoxViewModel: BookOutBoxViewModel,
    modifier: Modifier = Modifier
) {
    val user by bookOutBoxViewModel.user.collectAsState(initial = LocalUser())
    val boxUiState by bookOutBoxViewModel.boxUiState.collectAsState()
    val bookOutBoxUiState by bookOutBoxViewModel.bookOutBoxUiState.collectAsState()
    val scannedItemList by bookOutBoxViewModel.scannedItemList.collectAsState()
    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(bookOutBoxUiState.errorMessage) {
        showError = bookOutBoxUiState.errorMessage == null
    }

    LaunchedEffect(boxUiState.scannedBox,scannedItemList,Unit) {
        if (boxUiState.scannedBox.epc.isEmpty()) bookOutBoxViewModel.updateBookOutBoxErrorMessage("Please scan a box tag first!")
        else if (scannedItemList.isEmpty()) bookOutBoxViewModel.updateBookOutBoxErrorMessage("Pleas read an item first!")
        else bookOutBoxViewModel.updateBookOutBoxErrorMessage(null)
    }

    WarningDialog(
        icon = CustomIcon.Vector(Icons.Default.Error),
        message = bookOutBoxUiState.errorMessage ?: "",
        showDialog = showError,
        positiveButtonTitle = "try again",
        onPositiveButtonClick = {}
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
        }
    }
}