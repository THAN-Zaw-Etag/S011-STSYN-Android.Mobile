package com.etag.stsyn.ui.screen.other_operations.onsite_verification

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen

@Composable
fun OnsiteVerificationSaveScreen(
    onsiteVerificationViewModel: OnsiteVerificationViewModel,
    modifier: Modifier = Modifier
) {
    BaseSaveScreen(isError = true, onSave = { /*TODO*/ }) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
            Text(text = "Admin - 123S")
        }
    }
}