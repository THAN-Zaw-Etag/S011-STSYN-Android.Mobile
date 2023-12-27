@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.book_in_cal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.DetailBottomSheetScaffold
import com.etag.stsyn.ui.components.InfoBottomSheetContent
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.acct_check.DetailItem
import com.etag.stsyn.ui.screen.base.ParentScanScreen
import kotlinx.coroutines.launch

@Composable
fun BookInCalScanScreen() {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    DetailBottomSheetScaffold(
        state = scaffoldState,
        sheetContent = { BookInCalScanBottomSheetContent("Title") }) {
        ParentScanScreen(scannedItemCount = 5, onScan = { /*TODO*/ }, onClear = { /*TODO*/ }) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(10) {
                    ScannedItem(
                        id = "id",
                        name = "john smith",
                        showTrailingIcon = true,
                        onItemClick = {
                            if (scaffoldState.bottomSheetState.isVisible) coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                            else coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                        })
                }
            }
        }
    }
}

@Composable
private fun BookInCalScanBottomSheetContent(
    title: String,
    modifier: Modifier = Modifier
) {
    InfoBottomSheetContent(title = title) {
        LazyColumn {
            items(10) {
                DetailItem(title = "Hello", value = "World")
            }
        }
    }
}