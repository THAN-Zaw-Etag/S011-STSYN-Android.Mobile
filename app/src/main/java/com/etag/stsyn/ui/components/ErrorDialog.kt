package com.etag.stsyn.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties

@Composable
fun ErrorDialog(
    showDialog: Boolean,
    errorTitle: String,
    errorMessage: String,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = errorTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onError
                )
            },
            confirmButton = {
                Button(
                    onClick = onDismiss
                ) {
                    Text("OK")
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewErrorDialog() {
    Surface {
        ErrorDialog(
            showDialog = true,
            errorTitle = "Error",
            errorMessage = "Error message",
            onDismiss = {}
        )
    }
}