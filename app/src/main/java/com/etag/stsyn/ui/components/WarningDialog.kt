package com.etag.stsyn.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
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

@Composable
fun WarningDialog(
    icon: CustomIcon,
    message: String,
    color: Color = MaterialTheme.colorScheme.primary,
    showDialog: Boolean,
    positiveButtonTitle: String,
    onDismiss: () -> Unit = {},
    negativeButtonTitle: String = "",
    onNegativeButtonClick: () -> Unit = {},
    onPositiveButtonClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var show by remember { mutableStateOf(false) }

    LaunchedEffect(showDialog) {
        show = showDialog
    }

    if (show) {
        Dialog(properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        ), onDismissRequest = {
            show = false
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
                            Text(text = message, color = color)
                        }

                        Row(modifier = Modifier.align(Alignment.End)) {
                            TextButton(onClick = {
                                onPositiveButtonClick()
                                onDismiss()
                            }) {
                                Text(text = positiveButtonTitle.uppercase())
                            }

                            if (negativeButtonTitle.isNotEmpty()) {
                                TextButton(onClick = {
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
@Preview(showBackground = true)
fun WarningDialogPreview() {
    WarningDialog(
        positiveButtonTitle = "Yes",
        negativeButtonTitle = "No",
        icon = CustomIcon.Vector(Icons.Default.Warning),
        message = "You're able to scan unknown EPC",
        color = MaterialTheme.colorScheme.error,
        showDialog = true
    )
}