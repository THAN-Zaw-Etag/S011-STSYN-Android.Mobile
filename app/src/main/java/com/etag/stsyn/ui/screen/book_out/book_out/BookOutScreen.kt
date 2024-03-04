package com.etag.stsyn.ui.screen.book_out.book_out

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ConfirmationDialog
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.components.ScannedItem

@Composable
fun BookOutScreen(
    onClear: () -> Unit,
    scannedItems: List<Any>, // scanned items retrieved from scanner
    modifier: Modifier = Modifier
) {
    var showClearDialog by remember { mutableStateOf(false) }

    LaunchedEffect(scannedItems) {
        Log.d("TAG", "BookOutScreen: $scannedItems")
    }

    ConfirmationDialog(
        showDialog = showClearDialog,
        title = "Clear?",
        cancelTitle = "Cancel",
        confirmTitle = "Clear",
        onCancelClick = { showClearDialog = false },
        onConfirmClick = {
            showClearDialog = false
            onClear()
        })

    Column {
        LazyColumn(
            modifier = modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(10) {
                // scanned items here
                ScannedItem(
                    id = "SFE000$it",
                    name = "John Smith $it",
                    isScanned = false,
                    isSwipeable = true,
                    onSwipeToDismiss = {})
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Clear",
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    showClearDialog = true
                })
            ScanIconButton(onScan = {  })
            Text(text = "Total :${scannedItems.size}")
        }
    }

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun BookOutScreenPreview() {
    BookOutScreen(scannedItems = emptyList(), onClear = {})
}