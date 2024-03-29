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
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.components.ScreenWithBottomSheet
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.bottomsheet.BoxDetailScreen
import com.etag.stsyn.ui.theme.Purple80
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.util.ApiResponse
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnsiteVerificationScreen(
    onsiteVerificationViewModel: OnsiteVerificationViewModel,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current

    val hasScanned by onsiteVerificationViewModel.hasScanned.collectAsState()
    val scannedItems by onsiteVerificationViewModel.totalScannedItems.collectAsState()
    val outstandingItems by onsiteVerificationViewModel.outstandingItems.collectAsState()

    val onSiteVerifyItemState by onsiteVerificationViewModel.getOnSiteVerifyItems.collectAsState()
    val onsiteVerificationUiState by onsiteVerificationViewModel.onsiteVerificationUiState.collectAsState()

    val currentScanItem by onsiteVerificationViewModel.currentScannedItem.collectAsState()

    val scannedItemIndex by onsiteVerificationViewModel.scannedItemIndex.collectAsState()

    var boxItemsFromApi by remember { mutableStateOf<List<BoxItem>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val rfidUiState by onsiteVerificationViewModel.rfidUiState.collectAsState()
    val itemResultMessage by onsiteVerificationViewModel.filterStatusMessage.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var currentBoxItem by remember {
        mutableStateOf(BoxItem())
    }
    var isApiError by remember {
        mutableStateOf(false)
    }
    var retryCount by remember {
        mutableIntStateOf(0)
    }
    var apiErrorMessage by remember {
        mutableStateOf("")
    }


    when (onSiteVerifyItemState) {
        is ApiResponse.Loading -> {
            isApiError = false
            LoadingDialog(
                title = "Loading ... items...",
                showDialog = true,
                onDismiss = {  })
        }

        is ApiResponse.Success -> {
            isApiError = false
            boxItemsFromApi = onsiteVerificationUiState.allItemsFromApi
        }

        is ApiResponse.ApiError -> {
            isApiError = true
            val errorMessage = (onSiteVerifyItemState as ApiResponse.ApiError).message
            apiErrorMessage = errorMessage
        }

        else -> {
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


    ScreenWithBottomSheet(
        modifier = modifier,
        show = showBottomSheet,
        onDismiss = {showBottomSheet = false},
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
            if(hasScanned){
                ScannedContent(
                    onSwipeToDelete = {
                        onsiteVerificationViewModel.removeScannedBookInItemByIndex(it)
                    },
                    listState = listState,
                    boxItem = boxItemsFromApi,
                    onReset = {
                        onsiteVerificationViewModel.resetAll(isShowToast = true)
                    },
                    scannedItems = rfidUiState.scannedItems,
                    modifier = Modifier.weight(1f),
                    onItemClick = { _, _, boxItem ->
                        currentBoxItem = boxItem
                        showBottomSheet = true
                    })

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Os: ${outstandingItems.size}",
                        color = Color.Black
                    )
                    ScanIconButton(
                        isScanning = rfidUiState.isScanning,
                        onScan = {
                            if (boxItemsFromApi.isNotEmpty()) {
                                //onsiteVerificationViewModel.toggle()
                                onsiteVerificationViewModel.onReceivedTagIdTest()
                                coroutineScope.launch {
                                    if (scannedItemIndex != -1) listState.animateScrollToItem(
                                        scannedItemIndex
                                    )
                                }
                            }else{
                                Toast.makeText(context, "No items found in inventory", Toast.LENGTH_SHORT).show()
                            }
                        })
                    Text(
                        text = "Done: ${scannedItems.size}",
                        color = Color.Black
                    )
                }
            }else{
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Os: ${outstandingItems.size}",
                            color =  Color.Transparent
                        )
                        ScanIconButton(
                            isScanning = rfidUiState.isScanning,
                            onScan = {
                                if (boxItemsFromApi.isNotEmpty()) {
                                    //onsiteVerificationViewModel.toggle()
                                    onsiteVerificationViewModel.onReceivedTagIdTest()
                                    coroutineScope.launch {
                                        if (scannedItemIndex != -1) listState.animateScrollToItem(
                                            scannedItemIndex
                                        )
                                    }
                                }else{
                                    Toast.makeText(context, "No items found in inventory", Toast.LENGTH_SHORT).show()
                                }
                            })
                        Text(
                            text = "Done: ${scannedItems.size}",
                            color =  Color.Transparent
                        )
                    }
                }
            }


        }
    }
}

@Composable
private fun ScannedContent(
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
                fontWeight = FontWeight.Bold,

            )

            Text(
                text = "Reset",
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    showResetDialog = true
                },
                color = if(!boxItem.isNullOrEmpty())  Color.Black else Color.Transparent
            )
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
                        isSwipeable = false,
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