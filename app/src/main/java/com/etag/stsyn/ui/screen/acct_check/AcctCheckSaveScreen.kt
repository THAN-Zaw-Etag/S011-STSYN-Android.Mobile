package com.etag.stsyn.ui.screen.acct_check

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.etag.stsyn.ui.states.rememberMutableDialogState
import com.etag.stsyn.util.ErrorMessages
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun AcctCheckSaveScreen(
    accountCheckViewModel: AccountCheckViewModel,
    modifier: Modifier = Modifier
) {
    val user by accountCheckViewModel.userFlow.collectAsStateWithLifecycle(initialValue = LocalUser())
    val scannedItemIdList by accountCheckViewModel.scannedItemIdList.collectAsStateWithLifecycle()
    val saveAcctCheckResponse by accountCheckViewModel.saveAcctCheckResponse.collectAsStateWithLifecycle()
    val dialogState = rememberMutableDialogState(data = "")

    when (saveAcctCheckResponse) {
        is ApiResponse.ApiError -> dialogState.showDialog((saveAcctCheckResponse as ApiResponse.ApiError).message)
        else -> {}
    }

    WarningDialog(
        icon = CustomIcon.Vector(Icons.Default.Error),
        dialogState = dialogState,
        positiveButtonTitle = "Ok",
        onPositiveButtonClick = accountCheckViewModel::saveAccountabilityCheck
    )

    BaseSaveScreen(
        isError = scannedItemIdList.isEmpty(),
        errorMessage = if (scannedItemIdList.isEmpty()) ErrorMessages.READ_AN_ITEM_FIRST else "",
        onSave = accountCheckViewModel::saveAccountabilityCheck,
        modifier = modifier
    ) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
            Text(text = "${user.name} - ${user.nric}")
        }
    }
}