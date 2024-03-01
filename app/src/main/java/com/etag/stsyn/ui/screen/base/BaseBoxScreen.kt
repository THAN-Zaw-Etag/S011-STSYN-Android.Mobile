@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.etag.stsyn.ui.screen.base

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.rememberNavController
import com.etag.stsyn.ui.components.ConfirmationDialog
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.components.ScannedBoxItem
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.navigation.BottomSheetNavigation
import com.etag.stsyn.ui.navigation.BottomSheetNavigationGraph
import com.etag.stsyn.ui.theme.Purple80
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem

@Composable
fun BaseBoxScreen(
    modifier: Modifier = Modifier,
    bookItems: List<BoxItem>,
    boxes: List<BoxItem> = emptyList(), //TODO Need to remove empty list later
    scannedItemList: List<String> = emptyList(),
    isScanning: Boolean = false,
    boxOutTitle: String = "",
    checked: Boolean = false,
    scannedBox: BoxItem = BoxItem(),
    onReset: () -> Unit,
    onScan: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onCheckChange: (Boolean) -> Unit = {},
    showBoxBookOutButton: Boolean = false,
) {
    var items by remember { mutableStateOf(listOf<BoxItem>()) }
    var show by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var resetNavGraph by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val listState = rememberLazyListState()

    LaunchedEffect(bookItems) {
        if (bookItems.size > 1) listState.animateScrollToItem(bookItems.size - 1)
    }

    LaunchedEffect(bookItems, showBoxBookOutButton) {
        items = bookItems
        show = showBoxBookOutButton
    }

    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }, sheetState = sheetState) {
            BottomSheetContent(boxItems = boxes, resetNavGraph = resetNavGraph)
        }
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        val (content, buttonLayout) = createRefs()
        LazyColumn(contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState,
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 42.dp, bottom = 32.dp)
                .constrainAs(content) {
                    bottom.linkTo(buttonLayout.top)
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
            item {
                if (show) {
                    BoxBookOutButton(boxOutTitle) {

                        showBottomSheet = !showBottomSheet
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                BoxScanSection(
                    boxId = scannedBox.serialNo.uppercase(),
                    onRefresh = onRefresh,
                    boxDescription = scannedBox.description.uppercase()
                )
            }
            item {
                if (bookItems.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    ScannedItemsOptionLayout(
                        itemCount = items.size,
                        checked = checked,
                        onCheckChange = onCheckChange,
                        isScanned = false,
                        onReset = onReset
                    )
                }
            }

            itemsIndexed(bookItems) { index, item ->
                key(item.epc) {
                    ScannedItem(
                        id = "${item.serialNo} - ${item.itemLocation}",
                        name = item.description,
                        isScanned = item.epc in scannedItemList,
                    )
                }
            }
        }
        BottomScannedButtonLayout(
            outStandingItemCount = bookItems.size,
            scannedItemsCount = scannedItemList.size,
            isScanning = isScanning,
            onScan = onScan,
            modifier = Modifier.constrainAs(buttonLayout) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
    }
}

@Composable
private fun BottomSheetContent(
    resetNavGraph: Boolean, boxItems: List<BoxItem>
) {
    var bottomSheetNavController = rememberNavController()

    LaunchedEffect(resetNavGraph) {
        if (resetNavGraph) bottomSheetNavController.navigate(BottomSheetNavigation.BoxList.name)
    }

    BottomSheetNavigationGraph(navController = bottomSheetNavController, boxList = boxItems)
}

@Composable
fun BoxBookOutButton(title: String, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(5.dp))
        .clickable { onClick() }
        .background(Purple80.copy(alpha = 0.1f)),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            tint = Color.Gray,
            modifier = Modifier.padding(8.dp),
            contentDescription = null
        )
    }
}

@Composable
fun BottomScannedButtonLayout(
    outStandingItemCount: Int,
    scannedItemsCount: Int,
    isScanning: Boolean,
    modifier: Modifier = Modifier,
    onScan: () -> Unit
) {
    Row(
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Os: ${outStandingItemCount - scannedItemsCount}",
            modifier = Modifier.animateContentSize()
        )
        ScanIconButton(onScan = onScan, isScanning = isScanning)

        Text(
            text = "Done: $scannedItemsCount",
            modifier = Modifier.animateContentSize()
        )
    }
}

@Composable
private fun ScannedItemsOptionLayout(
    modifier: Modifier = Modifier,
    itemCount: Int,
    isScanned: Boolean = false,
    checked: Boolean,
    onCheckChange: (Boolean) -> Unit,
    onReset: () -> Unit,
) {
    var isChecked by remember { mutableStateOf(checked) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(checked) {
        isChecked = checked
    }

    ConfirmationDialog(showDialog = showConfirmationDialog,
        title = "Reset?",
        cancelTitle = "Cancel",
        confirmTitle = "Reset",
        onCancelClick = { showConfirmationDialog = false },
        onConfirmClick = {
            showConfirmationDialog = false
            onReset()
        })

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Items($itemCount)", fontWeight = FontWeight.Bold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Visual Check", fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = isChecked, onCheckedChange = {
                onCheckChange(it)
                isChecked = it
            })
        }

        TextButton(onClick = {
            if (itemCount != 0) showConfirmationDialog = true
        }) {
            Text(
                text = "Reset",
                color = if (itemCount != 0) MaterialTheme.colorScheme.primary else Color.DarkGray
            )
        }
    }
}

@Composable
private fun BoxScanSection(
    boxId: String, boxDescription: String, modifier: Modifier = Modifier, onRefresh: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        ScannedBoxItem(
            boxTitle = "Box",
            showRefreshIcon = false,
            boxDescription = boxId,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.3f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        ScannedBoxItem(
            boxTitle = "Description",
            boxDescription = boxDescription,
            showRefreshIcon = true,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.7f)
        )
    }
}