package com.etag.stsyn.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.theme.Purple80


@Composable
fun ShowBaseUrlAlertDialog(
    onConfirm: () -> Unit,
    onDismiss:()->Unit
) {
    var showAlertDialog by remember { mutableStateOf(true) }

    if (showAlertDialog){
        AlertDialog(
            onDismissRequest = { /* Dismiss the dialog when the user tries to dismiss it */ },
            title = { Text(text = "Base URL Missing") },
            text = { Text("The base URL is not set. Please configure it to proceed.") },
            confirmButton = {
                Button(
                    onClick = {
                    onConfirm()
                    showAlertDialog = false
                }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp, 16.dp, 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple80,
                        contentColor = Color.White
                    )

                ) {
                    Text("Set Base URL")
                }
            },
            dismissButton = {
            }
        )
    }

}

fun Modifier.alertDialog(): Modifier {
    return this
        .wrapContentWidth()
        .wrapContentHeight()
}
fun Modifier.textButton(): Modifier {
    return this
        .fillMaxWidth()
        .padding(16.dp, 8.dp, 16.dp, 0.dp)
}
