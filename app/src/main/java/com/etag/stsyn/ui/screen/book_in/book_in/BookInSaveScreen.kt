package com.etag.stsyn.ui.screen.book_in.book_in

import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.tooling.preview.Preview
import com.etag.stsyn.data.model.LocalUser
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import kotlinx.coroutines.delay

@Composable
fun BookInSaveScreen(
    viewModel: BookInViewModel,
    modifier: Modifier = Modifier,
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val user by viewModel.user.collectAsState(initial = LocalUser())

    LaunchedEffect(showConfirmationDialog) {
        delay(3000)
        showConfirmationDialog = false
    }

    LoadingDialog(
        title = "Loading...",
        showDialog = showConfirmationDialog,
        onDismiss = { })

    BaseSaveScreen(
        isError = false,
        modifier = modifier,
        onSave = { showConfirmationDialog = true }) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
            Text(text = "${user.name} - ${user.nric}")
        }
        SaveItemLayout(icon = Icons.Default.People, itemTitle = "Buddy") {
            Text(text = "-")
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun BookInSaveScreenPreview() {
}