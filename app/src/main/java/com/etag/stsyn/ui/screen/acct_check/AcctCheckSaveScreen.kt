package com.etag.stsyn.ui.screen.acct_check

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.tzh.retrofit_module.data.model.LocalUser

@Composable
fun AcctCheckSaveScreen(
    accountCheckViewModel: AccountCheckViewModel,
    modifier: Modifier = Modifier
) {
    val user by accountCheckViewModel.userFlow.collectAsState(initial = LocalUser())

    BaseSaveScreen(
        isError = false,
        onSave = { /*TODO*/ },
        modifier = modifier
    ) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
            Text(text = "${user.name} - ${user.userId}")
        }
    }
}