package com.etag.stsyn.ui.screen.book_in.t_loan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.components.ExpandedScannedItem
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.etag.stsyn.util.datasource.DataSource

@Composable
fun TLoanCountScreen(
    tLoanViewModel: TLoanViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by tLoanViewModel.rfidUiState.collectAsStateWithLifecycle()
    val items = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        DataSource.dummyDataList.forEach { items.add(it) }
    }

    BaseCountScreen(modifier = modifier, itemCount = items.size, onTabSelected = {}) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(items) {
                ExpandedScannedItem()
            }
        }
    }
}