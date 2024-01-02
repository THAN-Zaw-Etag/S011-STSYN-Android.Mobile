package com.etag.stsyn.ui.screen.book_in.t_loan_box

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen

@Composable
fun TLoanBoxSaveScreen(
    tLoanBoxViewModel: TLoanBoxViewModel,
    modifier: Modifier = Modifier
) {
    BaseSaveScreen(isError = false, onSave = { /*TODO*/ }) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User", showRefreshIcon = true) {
            Text(text = "Admin-123S")
        }
        SaveItemLayout(icon = Icons.Default.People, itemTitle = "Buddy", showRefreshIcon = true) {
            Text(text = "Admin1-456S")
        }
    }
}