
package com.etag.stsyn.ui.screen.other_operations.onsite_verification


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomSheetScaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomSheetScaffoldState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomSheetState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ControlType
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.components.listItems
import com.etag.stsyn.ui.screen.base.BaseCountScreen
import com.etag.stsyn.ui.screen.bottomsheet.BoxDetailScreen
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.util.ApiResponse
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OnsiteVerificationCountScreen(
    onsiteVerificationViewModel: OnsiteVerificationViewModel,
    ) {

    val scannedItems by onsiteVerificationViewModel.totalScannedItems.collectAsState()
    val itemsResponse by onsiteVerificationViewModel.getOnSiteVerifyItems.collectAsState()
    val outstandingItems by onsiteVerificationViewModel.outstandingItems.collectAsState()
    var items by remember { mutableStateOf<List<BoxItem>>(emptyList()) }
    var selectedItem by remember { mutableStateOf(BoxItem()) }

    val onsiteVerificationUiState by onsiteVerificationViewModel.onsiteVerificationUiState.collectAsState()

//    val scaffoldState = rememberBottomSheetScaffoldState(
//        bottomSheetState = SheetState(
//            skipPartiallyExpanded = true,
//            density = LocalDensity.current,
//            skipHiddenState = false
//        )
//    )

    val muScaffoldState = rememberBottomSheetScaffoldState(
        BottomSheetState(initialValue = BottomSheetValue.Collapsed,
            density = LocalDensity.current,
            )
    )

    val coroutineScope = rememberCoroutineScope()



    TestDetailBottomSheetScaffold(
        state = muScaffoldState,
        sheetContent = {
            BoxDetailScreen(boxItem = selectedItem)
        }) {
        BaseCountScreen(
            itemCount = items.size,
            onTabSelected = { controlType ->
                when (controlType) {
                    ControlType.All -> {
                        if (itemsResponse is ApiResponse.Success) {
                            items = onsiteVerificationUiState.allItemsFromApi
                        }
                    }

                    ControlType.Done -> {
                        items = scannedItems.map { it!! }
                    }

                    ControlType.Outstanding -> {
                        items = outstandingItems.map { it!! }
                    }
                }

            }


        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                listItems(items) {
                    ScannedItem(
                        id = it.epc,
                        name = it.description,
                        showTrailingIcon = true,
                        onItemClick = {
                            selectedItem = it
//                            if (scaffoldState.bottomSheetState.isVisible) coroutineScope.launch {
//                                scaffoldState.bottomSheetState.hide()
//                            }
//                            else coroutineScope.launch {
//                                scaffoldState.bottomSheetState.expand()
//                            }
                            coroutineScope.launch {
                                if (muScaffoldState.bottomSheetState.isCollapsed) {
                                    muScaffoldState.bottomSheetState.expand()
                                } else {
                                    muScaffoldState.bottomSheetState.collapse()
                                }
                            }
                        })
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TestDetailBottomSheetScaffold(
    state: BottomSheetScaffoldState,
    modifier: Modifier = Modifier,
    sheetContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    BottomSheetScaffold(
        scaffoldState = state,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            sheetContent()
        },
    ) {
        Column(modifier = modifier.padding(it)) {
            content()
        }
    }
}