@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.other_operations.det_p_loan_box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.etag.stsyn.util.datasource.DataSource

@Composable
fun OtherDetPLoanOutBoxCountScreen(
    otherDetPLoanBoxViewModel: OtherDetPLoanBoxViewModel,
    modifier: Modifier = Modifier
) {
    val items = remember { mutableStateListOf<String>() }
    val rfidUiState by otherDetPLoanBoxViewModel.rfidUiState.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        DataSource.dummyDataList.forEach {
            items.add(it)
        }
    }

    BaseCountScreen(modifier = modifier, itemCount = items.size, onTabSelected = {}) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) {
                ScannedItem(id = "Hello", name = "World", showTrailingIcon = true, onItemClick = {})
            }
        }
    }
}