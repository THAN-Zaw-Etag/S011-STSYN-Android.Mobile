package com.etag.stsyn.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AnotherDeviceHasBeenLoggedInDialog(
    message: String,
    onLogOut: () -> Unit
) {
    var show by remember { mutableStateOf(true) }

    WarningDialog(
        icon = CustomIcon.Vector(Icons.Default.Error),
        message = message,
        showDialog = show,
        positiveButtonTitle = "Log out",
        onPositiveButtonClick = {
            onLogOut()
            show = false
        }
    )
}