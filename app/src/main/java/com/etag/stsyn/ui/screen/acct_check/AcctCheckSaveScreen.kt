package com.etag.stsyn.ui.screen.acct_check

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun AcctCheckSaveScreen(
    accountCheckViewModel: AccountCheckViewModel,
    modifier: Modifier = Modifier
) {
    val user by accountCheckViewModel.userFlow.collectAsState(initial = LocalUser())
    val scannedItemIdList by accountCheckViewModel.scannedItemIdList.collectAsState()
    val saveAcctCheckResponse by accountCheckViewModel.saveAcctCheckResponse.collectAsState()
    var errorMessage by remember { mutableStateOf("") }

    when (saveAcctCheckResponse) {
        is ApiResponse.ApiError -> errorMessage = (saveAcctCheckResponse as ApiResponse.ApiError).message
        else -> {}
    }

    WarningDialog(
        icon = CustomIcon.Vector(Icons.Default.Error),
        message = errorMessage,
        showDialog = errorMessage.isNotEmpty(),
        positiveButtonTitle = "Ok",
        onPositiveButtonClick = {
            accountCheckViewModel.saveAccountabilityCheck()
            errorMessage = ""
        }
    )

    BaseSaveScreen(
        isError = scannedItemIdList.isEmpty(),
        errorMessage = if (scannedItemIdList.isEmpty()) "Please read an item first" else "",
        onSave = accountCheckViewModel::saveAccountabilityCheck,
        modifier = modifier
    ) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
            Text(text = "${user.name} - ${user.userId}")
        }
    }
}