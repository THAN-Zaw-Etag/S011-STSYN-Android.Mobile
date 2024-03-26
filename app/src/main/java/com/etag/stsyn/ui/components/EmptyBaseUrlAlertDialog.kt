package com.etag.stsyn.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.util.URLValidator


@Composable
fun ShowBaseUrlAlertDialog(
    onConfirm: (String) -> Unit
) {
    var showAlertDialog by remember { mutableStateOf(true) }
    var validationErrorMessage by remember { mutableStateOf("") }
    var baseUrl by remember { mutableStateOf("") }

    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = { /* Dismiss the dialog when the user tries to dismiss it */ },
            title = { Text(text = "Set Base URL") },
            text = {
                Column {
                    TextField(
                        value = baseUrl,
                        placeholder = { Text(text = "Enter base url") },
                        onValueChange = { baseUrl = it }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (validationErrorMessage.isNotEmpty()) ErrorText(text = validationErrorMessage)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        validationErrorMessage = URLValidator.validate(baseUrl)

                        if (validationErrorMessage.isEmpty()) {
                            onConfirm(baseUrl)
                            showAlertDialog = false
                        }

                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp, 16.dp, 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple80,
                        contentColor = Color.White
                    )

                ) {
                    Text("Set Base URL", color = Color.White)
                }
            },
            dismissButton = {

            }
        )
    }

}

@Preview
@Composable
private fun EmptyBaseUrlAlertDialogPreview() {
    ShowBaseUrlAlertDialog {

    }
}