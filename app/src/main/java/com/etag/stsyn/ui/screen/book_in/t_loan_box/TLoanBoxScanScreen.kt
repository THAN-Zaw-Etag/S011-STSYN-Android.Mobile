package com.etag.stsyn.ui.screen.book_in.t_loan_box

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.etag.stsyn.ui.screen.book_out.book_out_box.BoxScreen
import com.etag.stsyn.util.DataSource

@Composable
fun TLoanBoxScanScreen() {
    val items = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        DataSource.dummyDataList.forEach { items.add(it) }
    }

    BoxScreen(
        scannedItems = items,
        onReset = { /*TODO*/ },
        showBoxBookOutButton = true,
        boxOutTitle = "Box T-Loan out (${items.size})"
    )
}