@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailBottomSheetScaffold(
    state: BottomSheetScaffoldState,
    modifier: Modifier = Modifier,
    sheetContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    BottomSheetScaffold(
        scaffoldState = state,
        sheetPeekHeight = 0.dp, sheetContent = { sheetContent() },
    ) {
        Column(modifier = modifier.padding(it)) {
            content()
        }
    }
}