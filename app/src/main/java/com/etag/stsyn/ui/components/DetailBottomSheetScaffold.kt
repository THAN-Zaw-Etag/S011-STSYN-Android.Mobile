@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailBottomSheetScaffold(
    showBottomSheet: Boolean,
    state: BottomSheetScaffoldState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    sheetContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(skipHiddenState = false, skipPartiallyExpanded = true)
    )

    BottomSheetScaffold(
        scaffoldState = state,
        sheetPeekHeight = 0.dp, sheetContent = { sheetContent() },
    ) {
        Column(modifier = modifier.padding(it)) {
            content()
        }
    }
}