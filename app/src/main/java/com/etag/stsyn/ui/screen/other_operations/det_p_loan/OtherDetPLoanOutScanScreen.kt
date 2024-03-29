@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.other_operations.det_p_loan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.etag.stsyn.domain.model.DetPLoanItem
import com.etag.stsyn.ui.components.DetPLoanSwipeableItem
import com.etag.stsyn.ui.components.ScreenWithBottomSheet
import com.etag.stsyn.ui.screen.base.BaseScanScreen

@Composable
fun OtherDetPLoanScanScreen(
    otherDetPLoanViewModel: OtherDetPLoanViewModel,
    modifier: Modifier = Modifier
) {
    var showDetailBottomSheet by remember { mutableStateOf(false) }

    val rfidUiState by otherDetPLoanViewModel.rfidUiState.collectAsState()

    ScreenWithBottomSheet(
        modifier = modifier,
        show = showDetailBottomSheet,
        onDismiss = { showDetailBottomSheet = false },
        sheetContent = {
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .background(Color.White)
            ) {

            }
        }) {
        DetPLoanOutContent(
            items = rfidUiState.scannedItems,
            isScanning = rfidUiState.isScanning,
            onScan = {
                otherDetPLoanViewModel.toggle()
            },
            onItemClick = {
                showDetailBottomSheet = true
            },
            onSwipeToDismiss = { otherDetPLoanViewModel.removeItem(it) },
            onClear = { otherDetPLoanViewModel.removeScannedItems() }
        )
    }
}

@Composable
fun DetPLoanOutContent(
    items: List<String>,
    onItemClick: () -> Unit,
    isScanning: Boolean,
    onScan: () -> Unit,
    onSwipeToDismiss: (String) -> Unit,
    onClear: () -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(items) {
        if (items.size > 1) listState.animateScrollToItem(items.size - 1)
    }

    BaseScanScreen(
        scannedItemCount = items.size,
        onScan = onScan,
        isScanning = isScanning,
        onClear = onClear
    ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(items) { _, item ->
                key(item) {
                    DetPLoanSwipeableItem(
                        isSwipeable = true,
                        item = DetPLoanItem("", item, "", ""),
                        onItemClick = onItemClick,
                        onSwipeToDismiss = { onSwipeToDismiss(item) }
                    )
                }
            }
        }
    }
}