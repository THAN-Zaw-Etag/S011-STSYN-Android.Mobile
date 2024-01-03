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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.domain.model.DetPLoanItem
import com.etag.stsyn.ui.components.DetPLoanSwipeableItem
import com.etag.stsyn.ui.components.DetailBottomSheetScaffold
import com.etag.stsyn.ui.screen.BottomSheetContent
import com.etag.stsyn.ui.screen.base.BaseScanScreen
import com.etag.stsyn.util.DataSource
import kotlinx.coroutines.launch

@Composable
fun OtherTLoanOutScanScreen(
    otherTLoanOutViewModel: OtherTLoanOutViewModel,
    modifier: Modifier = Modifier
) {
    val items = remember { mutableStateListOf<String>() }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(skipPartiallyExpanded = false, skipHiddenState = false)
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        DataSource.dummyDataList.forEach {
            items.add(it)
        }
    }

    DetailBottomSheetScaffold(
        state = scaffoldState,
        sheetContent = { BottomSheetContent(itemList = listOf()) }) {
        BaseScanScreen(
            scannedItemCount = items.size,
            onScan = { /*TODO*/ },
            onClear = { /*TODO*/ }) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items) {
                    key(it) {
                        DetPLoanSwipeableItem(
                            isSwipeable = true,
                            item = DetPLoanItem(
                                "$it",
                                "data link jumper cable",
                                "tool",
                                "01010010"
                            ),
                            onItemClick = {
                                if (scaffoldState.bottomSheetState.isVisible) coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                                else coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                            },
                            onSwipeToDismiss = {}
                        )
                    }
                }
            }
        }
    }
}