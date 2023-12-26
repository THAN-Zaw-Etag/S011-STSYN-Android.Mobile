package com.etag.stsyn.ui.screen.book_out.book_out_box

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.etag.stsyn.ui.components.ConfirmationDialog
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.components.ScannedBoxItem
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun BoxScreen(
    scannedItems: List<String>,
    onReset: () -> Unit,
    showBoxBookOutButton: Boolean = false,
    modifier: Modifier = Modifier
) {

    var items by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(scannedItems) {
        items = scannedItems
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        val (content, buttonLayout) = createRefs()
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 42.dp, bottom = 32.dp)
                .constrainAs(content) {
                    bottom.linkTo(buttonLayout.top)
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            item {
                if (showBoxBookOutButton) {
                    BoxBookOutButton(count = 2) {}
                    Spacer(modifier = Modifier.height(16.dp))
                }
                BoxScanSection(
                    boxId = "C123456",
                    boxDescription = "ADAPTER SET AIRCRAFT MAINTENANCE (BOX)(R01) C123456"
                )
            }
            item {
                if (items.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    ScannedItemsOptionLayout(items.size, isScanned = false, onReset = onReset)
                }
            }
            itemsIndexed(scannedItems) { index, item ->
                ScannedItem(
                    id = item,
                    name = "Box 01 item 0${index + 1}",
                    isScanned = false,
                )
            }
        }
        BottomScannedButtonLayout(
            outStandingItemCount = 2,
            scannedItemsCount = 0,
            modifier = Modifier.constrainAs(buttonLayout) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {

        }
    }
}

@Composable
fun BoxBookOutButton(count: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .clickable { onClick() }
            .background(Purple80.copy(alpha = 0.1f)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Box booked out ($count)",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            tint = Color.Gray,
            modifier = Modifier.padding(8.dp),
            contentDescription = null
        )
    }
}

@Composable
fun BottomScannedButtonLayout(
    outStandingItemCount: Int,
    scannedItemsCount: Int,
    modifier: Modifier = Modifier,
    onScan: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (outStandingItemCount != 0) {
            Text(
                text = "Os: $outStandingItemCount"
            )
        }

        ScanIconButton(onScan = onScan)

        if (outStandingItemCount != 0) {
            Text(
                text = "Done: $scannedItemsCount"
            )
        }
    }
}

@Composable
private fun ScannedItemsOptionLayout(
    itemCount: Int,
    isScanned: Boolean = false,
    modifier: Modifier = Modifier,
    onReset: () -> Unit
) {
    var checked by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    ConfirmationDialog(
        showDialog = showConfirmationDialog,
        title = "Reset?",
        cancelTitle = "Cancel",
        confirmTitle = "Reset",
        onCancelClick = { showConfirmationDialog = false },
        onConfirmClick = {
            showConfirmationDialog = false
            onReset()
        })

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Items($itemCount)",
            fontWeight = FontWeight.Bold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Visual Check",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = checked, onCheckedChange = { checked = it })
        }

        Text(
            text = "Reset",
            color = if (isScanned) Purple80 else Color.DarkGray,
            modifier = Modifier.clickable {
                if (isScanned) showConfirmationDialog = true
            }
        )
    }
}

@Composable
private fun BoxScanSection(
    boxId: String,
    boxDescription: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
    ) {
        ScannedBoxItem(
            boxTitle = "Box", boxDescription = boxId, modifier = Modifier
                .fillMaxHeight()
                .weight(0.3f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        ScannedBoxItem(
            boxTitle = "Description", boxDescription = boxDescription, modifier = Modifier
                .fillMaxHeight()
                .weight(0.7f)
        )
    }
}