@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.etag.stsyn.domain.model.DetPLoanItem
import com.etag.stsyn.ui.components.DetPLoanSwipeableItem
import com.etag.stsyn.ui.components.DetailBottomSheetScaffold
import com.etag.stsyn.ui.screen.base.BaseScanScreen
import kotlinx.coroutines.launch

@Composable
fun OtherDetPLoanOutBoxScanScreen(
    otherDetPLoanBoxViewModel: OtherDetPLoanBoxViewModel,
    modifier: Modifier = Modifier
) {
    var showDetailBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    val items = remember {
        mutableStateListOf(
            DetPLoanItem("sn000001 - dljc111111", "data linkwq jumper cable", "tool", "01010011"),
            DetPLoanItem("sn000002 - dljc111111", "data linkew jumper cable", "tool", "01010011"),
            DetPLoanItem("sn000003 - dljc111111", "data linkwqew jumper cable", "tool", "01010011"),
            DetPLoanItem("sn000004 - dljc111111", "data linkwq jumper cable", "tool", "01010011"),
            DetPLoanItem("sn000005 - dljc111111", "data link dsjumper cable", "tool", "01010011"),
        )
    }

    DetailBottomSheetScaffold(
        state = scaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .background(Color.White)
            ) {

            }
        }) {
        DetPLoanOutContent(
            items = items,
            onItemClick = {
                if (scaffoldState.bottomSheetState.isVisible) {
                    coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                } else coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
            },
            onSwipeToDismiss = { items.remove(it) },
            onClear = { items.clear() }
        )
    }
}

@Composable
fun DetPLoanOutContent(
    items: List<DetPLoanItem>,
    onItemClick: () -> Unit,
    onSwipeToDismiss: (DetPLoanItem) -> Unit,
    onClear: () -> Unit,
) {
    BaseScanScreen(
        scannedItemCount = items.size,
        onScan = { },
        onClear = onClear
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(items) { index, item ->
                key(item.id) {
                    DetPLoanSwipeableItem(
                        isSwipeable = true,
                        item = item,
                        onItemClick = onItemClick,
                        onSwipeToDismiss = onSwipeToDismiss
                    )
                }
            }
        }
    }
}