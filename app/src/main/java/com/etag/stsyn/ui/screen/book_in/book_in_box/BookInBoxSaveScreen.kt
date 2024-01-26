package com.etag.stsyn.ui.screen.book_in.book_in_box

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.tzh.retrofit_module.data.model.LocalUser

@Composable
fun BookInBoxSaveScreen(
    bookInBoxViewModel: BookInBoxViewModel,
    modifier: Modifier = Modifier
) {
    val bookInBoxUiState by bookInBoxViewModel.bookInBoxUiState.collectAsState()
    val scannedItemList by bookInBoxViewModel.scannedItemsList.collectAsState()
    val user by bookInBoxViewModel.user.collectAsState(initial = LocalUser())

    BaseSaveScreen(
        isError = scannedItemList.isEmpty(),
        errorMessage = if (bookInBoxUiState.scannedBox.epc.isEmpty()) "Please read a box first" else "Please read an item first",
        modifier = modifier,
        onScan = {
            bookInBoxViewModel.apply {
                enableScan()
                updateScanType(BaseViewModel.ScanType.Single)
                toggle()
            }
        },
        onSave = { /*TODO*/ }) {
        SaveItemLayout(
            icon = Icons.Default.Person,
            itemTitle = "User",
        ) {
            Text(text = "${user.name}-${user.userId}")
        }

        if (true) { //TODO replace true with bookInBoxUiState.isUsCase
            SaveItemLayout(
                icon = Icons.Default.People,
                itemTitle = "Buddy",
                showRefreshIcon = true,
                onRefresh = {}
            ) {
                Text(text = "${bookInBoxUiState.issuerUser?.userName} - ${bookInBoxUiState.issuerUser?.userId}")
            }
        }
    }
}