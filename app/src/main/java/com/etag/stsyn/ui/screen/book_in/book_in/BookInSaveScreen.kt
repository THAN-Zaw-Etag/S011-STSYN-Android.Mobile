package com.etag.stsyn.ui.screen.book_in.book_in

import androidx.compose.material.icons.Icons
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
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.tzh.retrofit_module.data.model.LocalUser
import kotlinx.coroutines.delay

@Composable
fun BookInSaveScreen(
    viewModel: BookInViewModel,
    modifier: Modifier = Modifier,
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val user by viewModel.user.collectAsState(initial = LocalUser())
    val scannedBookInItems by viewModel.scannedBookInItems.collectAsState()

    LaunchedEffect(showConfirmationDialog) {
        delay(3000)
        showConfirmationDialog = false
    }

    LoadingDialog(
        title = "Loading...",
        showDialog = showConfirmationDialog,
        onDismiss = { })

    BaseSaveScreen(
        isError = scannedBookInItems.isEmpty(),
        errorMessage = "Please read an item first.",
        modifier = modifier,
        onSave = { showConfirmationDialog = true }) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
            Text(text = "${user.name} - ${user.nric}")
        }
    }
}