package com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ErrorText
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseScanScreen
import com.etag.stsyn.util.DataSource

@Composable
fun CheckInOutScreen(
    onsiteCheckInOutViewModel: OnsiteCheckInOutViewModel,
    modifier: Modifier = Modifier
) {
    val scannedItems = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        DataSource.dummyDataList.forEach {
            scannedItems.add(it)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = "Note: Issuer and receiver must be from the same flight"
        )
        BaseScanScreen(
            scannedItemCount = scannedItems.size,
            onScan = { /*TODO*/ },
            onClear = { /*TODO*/ }) {
            LazyColumn(
                modifier = Modifier,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(scannedItems.size) {
                    key(it) {
                        ScannedItem(
                            id = "$it",
                            isSwipeable = true,
                            name = "data link jumper cable".toUpperCase(),
                            onSwipeToDismiss = {}
                        )
                    }
                }
            }
        }
    }
}