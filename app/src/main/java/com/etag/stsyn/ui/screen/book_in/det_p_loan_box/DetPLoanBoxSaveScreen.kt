package com.etag.stsyn.ui.screen.book_in.det_p_loan_box

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
fun DetPLoanBoxSaveScreen(
    detPLoanBoxViewModel: DetPLoanBoxViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by detPLoanBoxViewModel.rfidUiState.collectAsStateWithLifecycle()

    BaseSaveScreen(isError = false, modifier = modifier, onSave = {  }) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User", showRefreshIcon = true) {
            Text(text = "Admin-123S")
        }
    }
}