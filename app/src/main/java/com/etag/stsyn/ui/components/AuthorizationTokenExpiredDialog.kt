package com.etag.stsyn.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.etag.stsyn.ui.states.rememberMutableDialogState

@Composable
fun AuthorizationTokenExpiredDialog(
    message: String,
    onLogOut: () -> Unit
) {
    var show by remember { mutableStateOf(true) }
    val state = rememberMutableDialogState(data = "")

     LaunchedEffect (message) {
         state.showDialog(message)
     }

    WarningDialog(
        icon = CustomIcon.Vector(Icons.Default.Error),
        dialogState = state,
        positiveButtonTitle = "Log out",
        onDismiss = {
            show = false
        },
        onPositiveButtonClick = {
            onLogOut()
            show = false
        }
    )
}