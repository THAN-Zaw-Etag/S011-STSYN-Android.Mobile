package com.etag.stsyn.ui.screen.other_operations.t_loan_out_box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.components.ScreenWithBottomSheet
import com.etag.stsyn.ui.screen.BottomSheetContent
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.etag.stsyn.util.datasource.DataSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherTLoanOutBoxCountScreen(
    otherTLoanOutBoxViewModel: OtherTLoanOutBoxViewModel,
    modifier: Modifier = Modifier
) {
    val items = remember { mutableStateListOf<String>() }
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        DataSource.dummyDataList.forEach {
            items.add(it)
        }
    }

    ScreenWithBottomSheet(
        show = showBottomSheet,
        onDismiss = { showBottomSheet = false },
        modifier = modifier,
        sheetContent = { BottomSheetContent(itemList = listOf()) }) {
        BaseCountScreen(itemCount = items.size, onTabSelected = {}) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items) {
                    ScannedItem(
                        id = "Hello",
                        name = "World",
                        showTrailingIcon = true,
                        onItemClick = { showBottomSheet = true })
                }
            }
        }
    }
}