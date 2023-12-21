package com.etag.stsyn.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.etag.stsyn.ui.theme.Purple80

/**
 * Confrimation Dialog
 * @param showDialog The default state of dialog
 * @param title The title of dialog
 * @param cancelTitle The title of cancel button
 * @param confirmTitle The title of confirmation button
 * @param onCancelClick Triggers action when click on cancel button
 * @param onConfirmClick Triggers action when click on confirmation button
 * */
@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    title: String,
    cancelTitle: String,
    confirmTitle: String,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var openDialog by remember { mutableStateOf(showDialog) }

    LaunchedEffect(showDialog) {
        openDialog = showDialog
    }

    if (openDialog) {
        Dialog(
            // dialog will only be dismissible on cancel click
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            onDismissRequest = { openDialog = false }
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp)),
                color = Color.White
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = title)
                        Spacer(modifier = Modifier.height(24.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                            ) {
                                TextButton(onClick = {
                                    onCancelClick()
                                    openDialog = false
                                }) {
                                    Text(text = cancelTitle, color = Purple80)
                                }

                                TextButton(onClick = onConfirmClick) {
                                    Text(text = confirmTitle, color = Purple80)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ConfirmationDialogPreview() {
    ConfirmationDialog(
        showDialog = true,
        title = "Clear?",
        cancelTitle = "Cancel",
        confirmTitle = "Clear",
        onCancelClick = {},
        onConfirmClick = {}
    )
}