@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.etag.stsyn.ui.screen.other_operations.t_loan_out_box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.domain.model.DetPLoanItem
import com.etag.stsyn.ui.components.DetPLoanSwipeableItem
import com.etag.stsyn.ui.components.DetailBottomSheetScaffold
import com.etag.stsyn.ui.screen.BottomSheetContent
import com.etag.stsyn.ui.screen.base.BaseScanScreen
import kotlinx.coroutines.launch

@Composable
fun OtherTLoanOutBoxScanScreen(
    otherTLoanOutBoxViewModel: OtherTLoanOutBoxViewModel,
    modifier: Modifier = Modifier
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(skipPartiallyExpanded = false, skipHiddenState = false)
    )
    val coroutineScope = rememberCoroutineScope()
    val rfidUiState by otherTLoanOutBoxViewModel.rfidUiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(rfidUiState.scannedItems) {
        if (rfidUiState.scannedItems.size > 1) listState.animateScrollToItem(rfidUiState.scannedItems.size - 1)
    }
    DetailBottomSheetScaffold(
        modifier = modifier,
        state = scaffoldState,
        sheetContent = { BottomSheetContent(itemList = listOf()) }) {
        BaseScanScreen(
            scannedItemCount = rfidUiState.scannedItems.size,
            isScanning = rfidUiState.isScanning,
            onScan = { otherTLoanOutBoxViewModel.toggle() },
            onClear = { otherTLoanOutBoxViewModel.removeScannedItems() }) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(rfidUiState.scannedItems) {
                    key(it) {
                        DetPLoanSwipeableItem(
                            swipeable = true,
                            item = DetPLoanItem(
                                it,
                                "data link jumper cable",
                                "tool",
                                "01010010"
                            ),
                            onItemClick = {
                                if (scaffoldState.bottomSheetState.isVisible) coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                                else coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                            },
                            onSwipeToDismiss = { detPLoanItem ->
                                otherTLoanOutBoxViewModel.removeItem(it)
                            }
                        )
                    }
                }
            }
        }
    }
}