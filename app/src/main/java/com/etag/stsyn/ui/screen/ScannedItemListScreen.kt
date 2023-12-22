package com.etag.stsyn.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.components.ScannedItem

@Composable
fun ScannedItemListScreen(
    items: List<String>, // will replace later
    modifier: Modifier = Modifier
) {

    var scannedItems = remember { items.toMutableStateList() }

    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
    ) {
        val (list, bottomLayout) = createRefs()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 40.dp)
                .constrainAs(list) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(bottomLayout.top)
                }
        ) {
            itemsIndexed(scannedItems) { index, item ->
                ScannedItem(
                    id = "SFE000$index",
                    name = "John Smith $index",
                    isScanned = false,
                    isSwipeable = true,
                    onSwipeToDismiss = {
                        scannedItems.remove(item)
                    })
            }
        }

        BottomScanButtonLayout(
            modifier = Modifier.constrainAs(bottomLayout) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
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

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ScannedItemListScreenPreview() {
    ScannedItemListScreen(
        items = listOf(
            "Hello",
            "Hello",
            "Hello",
            "Hello",
            "Hello",
            "Hello",
            "Hello",
        )
    )
}