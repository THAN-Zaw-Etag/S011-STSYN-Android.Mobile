@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.etag.stsyn.domain.model.ItemDetail
import com.etag.stsyn.ui.components.ControlType
import com.etag.stsyn.ui.components.DetailRow
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.components.SegmentedControl
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountScreen(
    items: List<String>, modifier: Modifier = Modifier, onControlTypeChange: (ControlType) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    val detailItems = listOf(
        ItemDetail("Serial No.", "SN001001"),
        ItemDetail("Serial No.", "SN001001"),
        ItemDetail("Serial No.", "SN001001"),
        ItemDetail("Serial No.", "SN001001"),
        ItemDetail("Serial No.", "SN001001"),
        ItemDetail("Serial No.", "SN001001"),
        ItemDetail("Serial No.", "SN001001"),
        ItemDetail("Last Item", "SN001001"),
    )

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = { BottomSheetContent(itemList = detailItems) },
    ) {
        CountScreenContent(items = items, onControlTypeChange = onControlTypeChange, onItemClick = {
            if (scaffoldState.bottomSheetState.isVisible) {
                coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
            } else coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
        }
        )
    }
}

@Composable
fun CountScreenContent(
    items: List<String>,
    onItemClick: () -> Unit,
    onControlTypeChange: (ControlType) -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.padding(16.dp)) {

        val (control, scannedItems, text) = createRefs()

        SegmentedControl(onTabSelected = onControlTypeChange,
            modifier = Modifier.constrainAs(control) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(vertical = 42.dp)
                .constrainAs(scannedItems) {
                    top.linkTo(control.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(text.top)
                }) {
            itemsIndexed(items) { index, item ->
                ScannedItem(
                    id = item,
                    name = "Box 01 item 0$index",
                    showTrailingIcon = true,
                    onItemClick = onItemClick
                )
            }
        }

        if (items.size > 0) {
            Text(text = "Total ${items.size}", modifier = Modifier.constrainAs(text) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            })
        }
    }
}

@Composable
fun BottomSheetContent(
    itemList: List<ItemDetail>
) {
    Column {
        Text(
            text = "Box 01 item 01",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(itemList) {
                DetailRow(itemDetail = it)
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun CountScreenPreview() {
}