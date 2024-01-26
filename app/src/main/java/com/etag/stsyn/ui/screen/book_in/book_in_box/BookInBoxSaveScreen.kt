package com.etag.stsyn.ui.screen.book_in.book_in_box

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen

@Composable
fun BookInBoxSaveScreen(
    bookInBoxViewModel: BookInBoxViewModel,
    modifier: Modifier = Modifier
) {
    val bookInBoxUiState by bookInBoxViewModel.bookInBoxUiState.collectAsState()
    val scannedItemList by bookInBoxViewModel.scannedItemsList.collectAsState()

    LaunchedEffect(bookInBoxUiState.issuerUser) {
        Log.d("TAG", "BookInBoxSaveScreen: ${bookInBoxUiState.issuerUser}")
    }

    BaseSaveScreen(
        isError = scannedItemList.isEmpty(),
        errorMessage = if (bookInBoxUiState.scannedBox.epc.isEmpty()) "Please read a box first" else "Please read an item first",
        modifier = modifier,
        onSave = { /*TODO*/ }) {
        SaveItemLayout(
            icon = Icons.Default.Person,
            itemTitle = "User",
        ) {
            Text(text = "Admin-123S")
        }

        if (bookInBoxUiState.isUsCase) {
            SaveItemLayout(
                icon = Icons.Default.People,
                itemTitle = "Buddy",
                showRefreshIcon = true,
                onRefresh = {}
            ) {
                Text(text = "-")
            }
        }
    }
}