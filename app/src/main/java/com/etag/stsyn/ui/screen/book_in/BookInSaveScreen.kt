package com.etag.stsyn.ui.screen.book_in

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ErrorText
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.SaveButton
import com.etag.stsyn.ui.components.SaveItemLayout
import kotlinx.coroutines.delay

@Composable
fun BookInSaveScreen(
    showSaveButton: Boolean,
    isUSCase: Boolean = false,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onSave: () -> Unit
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showConfirmationDialog) {
        delay(3000)
        showConfirmationDialog = false
    }

    LoadingDialog(
        title = "Loading...",
        showDialog = showConfirmationDialog,
        onDismiss = { })

    Column(modifier = modifier.fillMaxSize()) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
            Text(text = "Admin - 123S")
        }
        content()
        if (isUSCase) {
            SaveItemLayout(
                icon = Icons.Default.People,
                itemTitle = "Buddy",
                showRefreshIcon = true
            ) {
                Text(text = "-")
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        if (showSaveButton) {
            SaveButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                showConfirmationDialog = true
                onSave()
            }
        } else {
            ErrorText(
                text = "Please read an item first.",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun BookInSaveScreenPreview() {
    BookInSaveScreen(
        showSaveButton = true,
        isUSCase = true,
        onSave = {},
        content = {}
    )
}