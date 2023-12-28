@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.acct_check

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.DetailBottomSheetScaffold
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.BottomSheetContent
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.etag.stsyn.util.DataSource
import kotlinx.coroutines.launch

@Composable
fun AcctCheckCountScreen(
    modifier: Modifier = Modifier
) {
    val items = remember { mutableStateListOf<String>() }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(skipPartiallyExpanded = true, skipHiddenState = false)
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        DataSource.dummyDataList.forEach { items.add(it) }
    }

    DetailBottomSheetScaffold(
        state = scaffoldState,
        modifier = modifier,
        sheetContent = { BottomSheetContent(itemList = listOf()) }) {
        BaseCountScreen(itemCount = items.size, onTabSelected = {}) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(items) {
                    ScannedItem(
                        id = "Hello",
                        name = "World",
                        showTrailingIcon = true,
                        onItemClick = {
                            if (scaffoldState.bottomSheetState.isVisible) coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                            else coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                        })
                }
            }
        }
    }
}