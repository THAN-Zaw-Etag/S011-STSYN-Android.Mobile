package com.etag.stsyn.ui.screen.book_in.book_in

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
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
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.SuccessDialog
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.tzh.retrofit_module.data.mapper.toItemMovementLog
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.data.model.book_in.PrintJob
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.DateUtil
import kotlinx.coroutines.delay

@Composable
fun BookInSaveScreen(
    viewModel: BookInViewModel,
    modifier: Modifier = Modifier,
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val user by viewModel.user.collectAsState(initial = LocalUser())
    val scannedBookInItems by viewModel.scannedBookInItems.collectAsState()
    val saveBookInResponse by viewModel.savedBookInResponse.collectAsState()

    LaunchedEffect(showConfirmationDialog) {
        delay(3000)
        showConfirmationDialog = false
    }

    when (saveBookInResponse) {
        is ApiResponse.Loading -> LoadingDialog(
            title = "Please wait while SMS is processing your request...\n",
            showDialog = showConfirmationDialog,
            onDismiss = { showConfirmationDialog = false })

        is ApiResponse.Success -> {
            showConfirmationDialog = false
            SuccessDialog(showDialog = true, title = "SUCCESS!", onDoneClick = { /*TODO*/ })
        }

        is ApiResponse.ApiError -> {
            WarningDialog(
                icon = CustomIcon.Vector(Icons.Default.Error),
                message = (saveBookInResponse as ApiResponse.ApiError).message,
                showDialog = true,
                positiveButtonTitle = "exit",
                onPositiveButtonClick = {}
            )
        }

        else -> {}
    }

    BaseSaveScreen(
        isError = scannedBookInItems.isEmpty(),
        errorMessage = "Please read an item first.",
        modifier = modifier,
        onSave = {
            viewModel.saveBookIn(
                SaveBookInRequest(
                    itemMovementLogs = scannedBookInItems.map { it!! }.toItemMovementLog(0),
                    printJob = PrintJob(
                        date = DateUtil.getCurrentDate(),
                        handheldId = 0,
                        reportType = "",
                        userId = user.userId.toInt()
                    )
                )
            )
            showConfirmationDialog = true
        }) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
            Text(text = "${user.name} - ${user.nric}")
        }
    }
}