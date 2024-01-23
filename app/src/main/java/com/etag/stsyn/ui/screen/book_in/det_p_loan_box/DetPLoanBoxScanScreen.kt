package com.etag.stsyn.ui.screen.book_in.det_p_loan_box

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.screen.base.BaseBoxScreen

@Composable
fun DetPLoanBoxScanScreen(
    detPLoanBoxViewModel: DetPLoanBoxViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by detPLoanBoxViewModel.rfidUiState.collectAsState()

    BaseBoxScreen(
        modifier = modifier,
        bookItems = emptyList(),
        isScanning = rfidUiState.isScanning,
        onScan = { detPLoanBoxViewModel.toggle() },
        onReset = { detPLoanBoxViewModel.removeScannedItems() },
        showBoxBookOutButton = true,
        boxOutTitle = "Box P-Loan out (${rfidUiState.scannedItems.size})"
    )
}