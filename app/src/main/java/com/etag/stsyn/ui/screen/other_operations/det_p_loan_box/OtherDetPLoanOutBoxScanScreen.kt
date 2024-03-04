@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.etag.stsyn.ui.screen.other_operations.det_p_loan_box

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.etag.stsyn.domain.model.DetPLoanItem
import com.etag.stsyn.ui.components.DetPLoanSwipeableItem
import com.etag.stsyn.ui.components.ScreenWithBottomSheet
import com.etag.stsyn.ui.screen.base.BaseScanScreen
import kotlinx.coroutines.launch

@Composable
fun OtherDetPLoanOutBoxScanScreen(
    otherDetPLoanBoxViewModel: OtherDetPLoanBoxViewModel,
    modifier: Modifier = Modifier
) {
    var showDetailBottomSheet by remember { mutableStateOf(false) }

    val rfidUiState by otherDetPLoanBoxViewModel.rfidUiState.collectAsState()

    ScreenWithBottomSheet(
        show = showDetailBottomSheet,
        onDismiss = {showDetailBottomSheet = false},
        modifier = modifier,
        sheetContent = {
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .background(Color.White)
            ) {}
        }) {
        DetPLoanOutContent(
            items = rfidUiState.scannedItems,
            onItemClick = { showDetailBottomSheet = true },
            onSwipeToDismiss = { otherDetPLoanBoxViewModel.removeItem(it) },
            onScan = { otherDetPLoanBoxViewModel.toggle() },
            isScanning = rfidUiState.isScanning,
            onClear = { otherDetPLoanBoxViewModel.removeScannedItems() }
        )
    }
}

@Composable
fun DetPLoanOutContent(
    items: List<String>,
    onItemClick: () -> Unit,
    onScan: () -> Unit,
    isScanning: Boolean,
    onSwipeToDismiss: (String) -> Unit,
    onClear: () -> Unit,
) {
    BaseScanScreen(
        scannedItemCount = items.size,
        onScan = onScan,
        isScanning = isScanning,
        onClear = onClear
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(items) { index, item ->
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