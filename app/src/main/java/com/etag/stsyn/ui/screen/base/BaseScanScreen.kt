package com.etag.stsyn.ui.screen.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ConfirmationDialog
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun BaseScanScreen(
    scannedItemCount: Int,
    isScanning: Boolean = false,
    onScan: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    scanContent: @Composable () -> Unit
) {


    Column(modifier = modifier) {
        Box(modifier = Modifier.weight(1f)) {
            scanContent()
        }
        BottomScanButtonLayout(
            itemCount = scannedItemCount,
            isScanning = isScanning,
            onScan = onScan,
            onClear = onClear
        )
    }
}

@Composable
private fun BottomScanButtonLayout(
    itemCount: Int,
    isScanning: Boolean,
    onScan: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showClearDialog by remember { mutableStateOf(false) }

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

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Clear",
            color = if (itemCount > 0) Purple80 else Color.DarkGray,
            textDecoration = TextDecoration.Underline,
            modifier = if (itemCount > 0) Modifier.clickable {
                showClearDialog = true
            } else Modifier)
        ScanIconButton(onScan = onScan, isScanning = isScanning)
        Text(text = "Total $itemCount")
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ParentScanScreenPreview() {
    BaseScanScreen(scannedItemCount = 5, onScan = { /*TODO*/ }, onClear = { /*TODO*/ }) {
        Text(text = "Hello")
    }
}