@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.other_operations.t_loan_out

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.unit.dp
import com.etag.stsyn.domain.model.DetPLoanItem
import com.etag.stsyn.ui.components.DetPLoanSwipeableItem
import com.etag.stsyn.ui.components.ScreenWithBottomSheet
import com.etag.stsyn.ui.screen.BottomSheetContent
import com.etag.stsyn.ui.screen.base.BaseScanScreen
import kotlinx.coroutines.launch

@Composable
fun OtherTLoanOutScanScreen(
    otherTLoanOutViewModel: OtherTLoanOutViewModel,
    modifier: Modifier = Modifier
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val rfidUiState by otherTLoanOutViewModel.rfidUiState.collectAsState()

    ScreenWithBottomSheet(
        show = showBottomSheet,
        onDismiss = {showBottomSheet = false},
        sheetContent = { BottomSheetContent(itemList = listOf()) }) {
        BaseScanScreen(
            scannedItemCount = rfidUiState.scannedItems.size,
            isScanning = rfidUiState.isScanning,
            onScan = { otherTLoanOutViewModel.toggle() },
            onClear = { otherTLoanOutViewModel.removeScannedItems() }) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(rfidUiState.scannedItems) {
                    key(it) {
                        DetPLoanSwipeableItem(
                            isSwipeable = true,
                            item = DetPLoanItem(
                                "$it",
                                "data link jumper cable",
                                "tool",
                                "01010010"
                            ),
                            onItemClick = { showBottomSheet = true },
                            onSwipeToDismiss = {}
                        )
                    }
                }
            }
        }
    }
}