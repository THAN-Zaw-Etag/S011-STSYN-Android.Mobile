package com.etag.stsyn.ui.screen.book_in.t_loan_box

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.screen.base.BaseBoxScreen

@Composable
fun TLoanBoxScanScreen(
    tLoanBoxViewModel: TLoanBoxViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by tLoanBoxViewModel.rfidUiState.collectAsState()

    BaseBoxScreen(
        bookItems = emptyList(),
        modifier = modifier,
        isScanning = rfidUiState.isScanning,
        onReset = { tLoanBoxViewModel.removeScannedItems() },
        onScan = { tLoanBoxViewModel.toggle() },
        showBoxBookOutButton = true,
        boxOutTitle = "Box T-Loan out (${rfidUiState.scannedItems.size})"
    )
}