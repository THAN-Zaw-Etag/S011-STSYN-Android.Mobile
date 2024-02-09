package com.etag.stsyn.ui.screen.book_in.book_in

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.etag.stsyn.ui.states.rememberMutableDialogState
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.util.API_MULTI_ATTEMPT_FAILED_MESSAGE
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.SAVING_MESSAGE

@Composable
fun BookInSaveScreen(
    viewModel: BookInViewModel,
    modifier: Modifier = Modifier,
) {

    var showSuccessDialog by remember { mutableStateOf(false) }
    val user by viewModel.userFlow.collectAsState(initial = LocalUser())
    val saveBookInResponse by viewModel.savedBookInResponse.collectAsState()
    val scannedItemIdList by viewModel.scannedItemIdList.collectAsState()
    var attemptCount by remember { mutableStateOf(0) }
    val dialogState = rememberMutableDialogState(data = "")

    when (saveBookInResponse) {
        is ApiResponse.Loading -> LoadingDialog(title = SAVING_MESSAGE,
            showDialog = true,
            onDismiss = { }
        )

        is ApiResponse.ApiError -> dialogState.showDialog((saveBookInResponse as? ApiResponse.ApiError)?.message ?: "")

        else -> {}
    }

    // show error dialog when error occurs in saving book in items
    WarningDialog(
        icon = CustomIcon.Vector(Icons.Default.Error),
        dialogState = dialogState,
        positiveButtonTitle = "exit",)

    BaseSaveScreen(
        isError = scannedItemIdList.isEmpty(),
        errorMessage = "Please read an item first.",
        modifier = modifier,
        onSave = viewModel::saveBookIn
    ) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
            Text(text = "${user.name} - ${user.nric}")
        }
    }
}