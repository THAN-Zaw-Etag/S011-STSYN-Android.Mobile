package com.etag.stsyn.ui.screen.book_in.book_in

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.etag.stsyn.ui.states.rememberMutableDialogState
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.SAVING_MESSAGE

@Composable
fun BookInSaveScreen(
    viewModel: BookInViewModel,
    modifier: Modifier = Modifier,
) {

    val user by viewModel.userFlow.collectAsStateWithLifecycle(LocalUser())
    val saveBookInResponse by viewModel.savedBookInResponse.collectAsStateWithLifecycle()
    val scannedItemIdList by viewModel.scannedItemIdList.collectAsStateWithLifecycle()
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