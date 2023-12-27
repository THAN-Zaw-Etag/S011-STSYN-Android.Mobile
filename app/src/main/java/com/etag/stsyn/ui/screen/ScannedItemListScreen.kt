package com.etag.stsyn.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.components.ScannedItem

@Composable
fun ScannedItemListScreen(
    items: List<String>, // will replace later
    showTrailingIcon: Boolean = false,
    modifier: Modifier = Modifier
) {
    var scannedItems = remember { mutableStateListOf(String()) }

    LaunchedEffect(items) {
        items.forEach { scannedItems.add(it) }
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            itemsIndexed(scannedItems) { index, item ->
                ScannedItem(
                    id = "SFE000$index",
                    name = "John Smith $index",
                    isScanned = false,
                    isSwipeable = true,
                    showTrailingIcon = showTrailingIcon,
                    onSwipeToDismiss = {
                        scannedItems.remove(item)
                    }
                )
            }
        }

        BottomScanButtonLayout(
            modifier = Modifier,
            itemCount = items.size,
            onScan = { /*TODO*/ },
            onClear = { /*TODO*/ }
        )
    }
}

@Composable
fun BottomScanButtonLayout(
    itemCount: Int,
    onScan: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Clear",
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onClear() })
        ScanIconButton(onScan = onScan)
        Text(text = "Total $itemCount")
    }
}
