package com.etag.stsyn.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable


@Composable
fun ShowBaseUrlAlertDialog(
    onConfirm: () -> Unit,
    onDismiss:()->Unit
) {
    AlertDialog(
        onDismissRequest = { /* Dismiss the dialog when the user tries to dismiss it */ },
        title = { Text(text = "Base URL Missing") },
        text = { Text("The base URL is not set. Please configure it to proceed.") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Set Base URL")
            }
        },
        dismissButton = {
//            Button(onClick = {
//                onDismiss()
//            }) {
//                Text("Cancel")
//            }
        }
    )
}