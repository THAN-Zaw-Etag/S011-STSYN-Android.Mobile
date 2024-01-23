package com.etag.stsyn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoadingDialog(
    title: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var openDialog by remember { mutableStateOf(showDialog) }

    LaunchedEffect(showDialog) {
        openDialog = showDialog
    }

    if (openDialog) {
        Dialog(
            onDismissRequest = {
                openDialog = false
                onDismiss()
            }, properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Surface(
                modifier = modifier.fillMaxWidth(0.9f),
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }

}


@Composable
fun LoginProgressDialog(showDialog: Boolean) {
    if (showDialog) {
        Dialog(onDismissRequest = { /* Dialog cannot be dismissed by clicking outside */ }) {
            // To make the dialog take up the full screen
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color(0x66000000))) { // Semi-transparent background
                // Dialog content
                Card(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    elevation =CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Please wait...",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDialog() {
    LoginProgressDialog(showDialog = true)
}