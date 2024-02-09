@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.etag.stsyn.ui.screen.other_operations.onsite_verification

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ConfirmationDialog
import com.etag.stsyn.ui.components.DetailBottomSheetScaffold
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.bottomsheet.BoxDetailScreen
import com.etag.stsyn.ui.theme.Purple80
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.util.ApiResponse
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnsiteVerifyScreen(
    onsiteVerificationViewModel: OnsiteVerificationViewModel,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current

    var hasScanned by remember { mutableStateOf(true) }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = false,
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )

    val scannedItems by onsiteVerificationViewModel.totalScannedItems.collectAsState()
    val outstandingItems by onsiteVerificationViewModel.outstandingItems.collectAsState()

    val getItemWhereNotInResponseStat by onsiteVerificationViewModel.getOnSiteVerifyItems.collectAsState()
    val onsiteVerificationUiState by onsiteVerificationViewModel.onsiteVerificationUiState.collectAsState()

    val currentScanItem by onsiteVerificationViewModel.currentScannedItem.collectAsState()

    val scannedItemIndex by onsiteVerificationViewModel.scannedItemIndex.collectAsState()

    var boxItemsFromApi by remember { mutableStateOf<List<BoxItem>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val rfidUiState by onsiteVerificationViewModel.rfidUiState.collectAsState()
    val itemResultMessage by onsiteVerificationViewModel.filterStatusMessage.collectAsState()


    var currentBoxItem by remember {
        mutableStateOf(BoxItem())
    }
    var isApiError by remember {
        mutableStateOf(false)
    }
    var retryCount by remember {
        mutableStateOf(0)
    }
    var apiErrorMessage by remember {
        mutableStateOf("")
    }


    when (getItemWhereNotInResponseStat) {
        is ApiResponse.Loading -> {
            Log.d("OnsiteVerifyScreen", "OnsiteVerifyScreen: Loading...")
            isApiError = false
            LoadingDialog(
                title = "Loading ... items...",
                showDialog = true,
                onDismiss = { /*TODO*/ })
        }

        is ApiResponse.Success -> {
            hasScanned = true

            Log.d("OnsiteVerifyScreen", "OnsiteVerifyScreen: Success...")
            isApiError = false
            //onsiteVerificationViewModel.updateUiState(items)
            boxItemsFromApi = onsiteVerificationUiState.allItemsFromApi
        }

        is ApiResponse.ApiError -> {
            Log.d("OnsiteVerifyScreen", "OnsiteVerifyScreen: ApiError...")
            isApiError = true
            val errorMessage = (getItemWhereNotInResponseStat as ApiResponse.ApiError).message
            apiErrorMessage = errorMessage
            //  onsiteVerificationViewModel.updateUiState(emptyList())
        }

        else -> {
            Log.d("OnsiteVerifyScreen", "OnsiteVerifyScreen: Else...")
            isApiError = false
        }
    }

    LaunchedEffect(itemResultMessage) {
        if (!itemResultMessage.isNullOrEmpty()) {
            Toast.makeText(context, "$itemResultMessage", Toast.LENGTH_SHORT).show()
        }
    }

    if (isApiError) {
        WarningDialog(
            attemptAccount = retryCount,
            message = apiErrorMessage,
            onProcess = {
                retryCount++
                onsiteVerificationViewModel.getOnSiteVerifyItems()
            }, onDismiss = { retryCount = 0 })
    }


    DetailBottomSheetScaffold(
        modifier = modifier,
        state = scaffoldState,
        sheetContent = {
            BoxDetailScreen(boxItem = currentBoxItem)
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ScannedBoxSection(
                id = currentScanItem?.epc ?: "",
                description = currentScanItem?.description ?: ""
            )
            if (hasScanned) {
                ScannedContent(
                    onSwipeToDelete = {
                        // It is not good for UX, Because it can just delete for Item from API
                        //   onsiteVerificationViewModel.removeScannedBookInItem(it)
                        onsiteVerificationViewModel.removeScannedBookInItemByIndex(it)
                    },
                    listState = listState,
                    boxItem = boxItemsFromApi,
                    onReset = {
                        onsiteVerificationViewModel.resetAll()
                    },
                    scannedItems = rfidUiState.scannedItems,
                    modifier = Modifier.weight(1f),
                    onItemClick = { epc, isScanned, boxItem ->
                        currentBoxItem = boxItem
                        if (scaffoldState.bottomSheetState.isVisible) coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                        else coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                    })
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Os: ${outstandingItems.size}",
                    color = if (hasScanned) Color.Black else Color.Transparent
                )
                ScanIconButton(
                    isScanning = rfidUiState.isScanning,
                    onScan = {
                        //  onsiteVerificationViewModel.toggle()
                        Log.d("@click", "OnsiteVerifyScreen: Clicked ${boxItemsFromApi.size}")

                        //TODO setup empty message dialog

                        if (boxItemsFromApi.isNotEmpty()) {

                           // onsiteVerificationViewModel.onReceivedTagIdTest()
                            onsiteVerificationViewModel.toggle()

                            coroutineScope.launch {
                                if (scannedItemIndex != -1) listState.animateScrollToItem(
                                    scannedItemIndex
                                )
                            }
                        }
                    })
                Text(
                    text = "Done: ${scannedItems.size}",
                    color = if (hasScanned) Color.Black else Color.Transparent
                )
            }
        }
    }
}

@Composable
private fun ScannedContent(
    // pass item list here
    onSwipeToDelete: (Int) -> Unit,
    listState: LazyListState,
    boxItem: List<BoxItem>?,
    scannedItems: List<String>,
    onReset: () -> Unit,
    onItemClick: (String, Boolean, BoxItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var showResetDialog by remember { mutableStateOf(false) }

    LaunchedEffect(scannedItems) {
        if (scannedItems.size > 1) listState.animateScrollToItem(scannedItems.size - 1)
    }

    Column(modifier = modifier) {
        ConfirmationDialog(
            showDialog = showResetDialog,
            title = "Reset",
            cancelTitle = "Cancel",
            confirmTitle = "Reset",
            onCancelClick = { showResetDialog = false },
            onConfirmClick = {
                showResetDialog = false
                onReset()

            })

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Item booked out ${boxItem?.size}",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Reset",
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    showResetDialog = true
                })
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (boxItem != null) {
                itemsIndexed(items = boxItem) { index, item ->
                    ScannedItem(
                        isScanned = item.isScanned,
                        id = "${item.serialNo} - ${item.itemLocation}",
                        isSwipeable = true,
                        name = item.description,
                        showTrailingIcon = true,
                        onItemClick = {
                            onItemClick(item.epc, item.isScanned, item)
                        },
                        onSwipeToDismiss = {
                            onSwipeToDelete(index)
                        }
                    )
                }
            }

            //TODO Don't know why it has to be used [KZL]
            /*else {
                items(scannedItems) {
                    ScannedItem(
                        id = it,
                        name = "data link jumper cable",
                        showTrailingIcon = true,
                        onItemClick = {
                            onItemClick(it, false, BoxItem())
                        }
                    )
                }
            }*/


        }
    }
}

@Composable
fun ScannedBoxSection(
    id: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Purple80.copy(0.1f))
            .padding(16.dp)
    ) {
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.DarkGray)) {
                append("Item Description: ")
            }
            withStyle(style = SpanStyle(color = Color.Black)) {
                append(if (description.isEmpty()) "-" else description.uppercase())
            }
        })

        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.DarkGray)) {
                append("ID: ")
            }
            withStyle(style = SpanStyle(color = Color.Black)) {
                append(if (id.isEmpty()) "-" else id.uppercase())
            }
        })
    }
}