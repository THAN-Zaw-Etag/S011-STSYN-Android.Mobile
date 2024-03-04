package com.etag.stsyn.ui.screen.book_in.t_loan

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen

@Composable
fun TLoanSaveScreen(
    tLoanViewModel: TLoanViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by tLoanViewModel.rfidUiState.collectAsStateWithLifecycle()

    BaseSaveScreen(modifier = modifier, isError = false, onSave = {  }) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
            Text(text = "Admin - 123S")
        }
    }
}