package com.etag.stsyn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.etag.stsyn.ui.theme.SuccessColor

@Composable
fun SuccessDialog(
    showDialog: Boolean,
    title: String,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var openDialog by remember { mutableStateOf(showDialog) }

    LaunchedEffect(showDialog) {
        openDialog = showDialog
    }

    if (openDialog) {
        Dialog(onDismissRequest = { openDialog = false }) {
            Surface(
                modifier = modifier.fillMaxWidth(0.9f),
                color = Color.Transparent
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(20)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            color = SuccessColor
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        FilledTonalButton(
                            onClick = onDoneClick,
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = SuccessColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(text = "OK", color = Color.White)
                        }
                    }

                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = SuccessColor,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(bottom = 16.dp)
                            .size(62.dp)
                            .background(color = Color.White, shape = CircleShape)
                            .border(
                                width = 5.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SuccessDialogPreview() {
    SuccessDialog(showDialog = true, title = "Success", onDoneClick = { /*TODO*/ })
}