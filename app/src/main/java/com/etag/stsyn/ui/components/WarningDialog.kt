package com.etag.stsyn.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
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
import com.etag.stsyn.ui.states.MutableDialogState
import com.etag.stsyn.ui.states.rememberMutableDialogState

@Composable
fun WarningDialog(
    icon: CustomIcon,
    dialogState: MutableDialogState<String>,
    color: Color = MaterialTheme.colorScheme.primary,
    positiveButtonTitle: String,
    onDismiss: () -> Unit = {},
    negativeButtonTitle: String = "",
    onNegativeButtonClick: () -> Unit = {},
    onPositiveButtonClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    if (dialogState.isVisible.value) {
        Dialog(properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        ), onDismissRequest = {
            dialogState.hideDialog()
            onDismiss()
        }) {
            Surface(
                modifier = modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp)),
                color = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            when (icon) {
                                is CustomIcon.Resource -> StsynIcon(
                                    icon = icon.iconRes, color = color
                                )

                                is CustomIcon.Vector -> StsynIcon(
                                    icon = icon.iconVector, color = color
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = dialogState.data.value, color = color)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.align(Alignment.End)) {
                            TextButton(
                                onClick = {
                                dialogState.hideDialog()
                                onPositiveButtonClick()
                                onDismiss()
                            }) {
                                Text(text = positiveButtonTitle.uppercase())
                            }

                            if (negativeButtonTitle.isNotEmpty()) {
                                TextButton(onClick = {
                                    dialogState.hideDialog()
                                    onNegativeButtonClick()
                                    onDismiss()
                                }) {
                                    Text(text = negativeButtonTitle.uppercase())
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
fun WarningDialog(
    attemptAccount: Int,
    message: String,
    onProcess: () -> Unit,
    onDismiss: () -> Unit
) {
    var showDialog by remember {
        mutableStateOf(true)
    }

    Log.d("WarningDialog", "attemptCount: $attemptAccount")
    var messageStatus = ""
    messageStatus = if (attemptAccount > 3){
        "Too many attempts,please check your internet connection or try again later"
    } else {
        message
    }

    if (showDialog) {
        Dialog(onDismissRequest = {
            showDialog = false
            onDismiss()
        }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp)),
                color = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StsynIcon(
                                icon = Icons.Default.Warning,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = messageStatus,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Row(modifier = Modifier.align(Alignment.End)) {
                            if (attemptAccount <= 2){
                                TextButton(onClick = {
                                    showDialog = false
                                    onProcess()
                                }) {
                                    Text(text = "try again".uppercase())
                                }
                            }else{
                                TextButton(onClick = {
                                    showDialog = false
                                    onDismiss()
                                }) {
                                    Text(text = "Ok".uppercase())
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}