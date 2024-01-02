package com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen

@Composable
fun CheckInOutSaveScreen(
    onsiteCheckInOutViewModel: OnsiteCheckInOutViewModel,
    modifier: Modifier = Modifier
) {
    val hasScannedItems by remember { mutableStateOf(false) }

    BaseSaveScreen(isError = false, onSave = { /*TODO*/ }) {
        SaveItemLayout(
            icon = Icons.Default.Person,
            itemTitle = "Issuer",
        ) {
            Text(text = "Admin-123S")
        }
        SaveItemLayout(
            icon = Icons.Default.Person,
            itemTitle = "Receiver",
            showRefreshIcon = hasScannedItems
        ) {
            Text(text = "-")
        }
    }
}
