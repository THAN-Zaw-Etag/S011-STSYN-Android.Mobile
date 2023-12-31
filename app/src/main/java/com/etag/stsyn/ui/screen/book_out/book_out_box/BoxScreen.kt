@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.book_out.book_out_box

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.etag.stsyn.ui.components.DetailBottomSheetScaffold
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.components.ScannedBoxItem
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.navigation.BottomSheetNavigationGraph
import com.etag.stsyn.ui.theme.Purple80
import kotlinx.coroutines.launch

@Composable
fun BoxScreen(
    scannedItems: List<String>,
    isScanning: Boolean = false,
    boxOutTitle: String = "",
    onReset: () -> Unit,
    onScan: () -> Unit = {},
    showBoxBookOutButton: Boolean = false,
    modifier: Modifier = Modifier
) {
    var items by remember { mutableStateOf(listOf<String>()) }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var show by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(scannedItems) {
        if (scannedItems.size > 1) listState.animateScrollToItem(scannedItems.size - 1)
    }

    LaunchedEffect(scannedItems, showBoxBookOutButton) {
        items = scannedItems
        show = showBoxBookOutButton
    }

    DetailBottomSheetScaffold(state = scaffoldState, sheetContent = {
        BottomSheetContent(boxItems = listOf(), onItemClick = {

        })
    }) {
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
                            if (scaffoldState.bottomSheetState.isVisible) {
                                coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                            } else coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    BoxScanSection(
                        boxId = "C123456".uppercase(),
                        boxDescription = "ADAPTER SET AIRCRAFT MAINTENANCE (BOX)(R01) C123456".uppercase()
                    )
                }
                item {
                    if (scannedItems.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        ScannedItemsOptionLayout(items.size, isScanned = false, onReset = onReset)
                    }
                }
                itemsIndexed(scannedItems) { index, item ->
                    ScannedItem(
                        id = item,
                        name = "Box 01 item 0${index + 1}",
                        isScanned = false,
                    )
                }
            }
            BottomScannedButtonLayout(outStandingItemCount = 2,
                scannedItemsCount = 0,
                isScanning = isScanning,
                onScan = onScan,
                modifier = Modifier.constrainAs(buttonLayout) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        }
    }
}

@Composable
private fun BottomSheetContent(
    onItemClick: () -> Unit,
    boxItems: List<String>, modifier: Modifier = Modifier
) {
    BottomSheetNavigationGraph(navController = rememberNavController())
}

@Composable
fun BookOutBoxItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Purple80.copy(0.1f), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable { },
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "C123456".uppercase(), color = Purple80)
            IconButton(onClick = { onClick() }) {
                Icon(
                    imageVector = Icons.Default.Storage,
                    tint = Purple80,
                    contentDescription = null
                )
            }
        }
        Text(text = "adapter set aircraft maintenance".uppercase(), color = Purple80)
        Text(text = "(box)(r01)c123456".uppercase(), color = Purple80)
    }
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
            text = title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
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
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (outStandingItemCount != 0) {
            Text(
                text = "Os: $outStandingItemCount"
            )
        }

        ScanIconButton(onScan = onScan, isScanning = isScanning)

        if (outStandingItemCount != 0) {
            Text(
                text = "Done: $scannedItemsCount"
            )
        }
    }
}

@Composable
private fun ScannedItemsOptionLayout(
    itemCount: Int, isScanned: Boolean = false, modifier: Modifier = Modifier, onReset: () -> Unit
) {
    var checked by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

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
            Switch(checked = checked, onCheckedChange = { checked = it })
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
    boxId: String, boxDescription: String, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
    ) {
        ScannedBoxItem(
            boxTitle = "Box",
            boxDescription = boxId,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.3f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        ScannedBoxItem(
            boxTitle = "Description",
            boxDescription = boxDescription,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.7f)
        )
    }
}