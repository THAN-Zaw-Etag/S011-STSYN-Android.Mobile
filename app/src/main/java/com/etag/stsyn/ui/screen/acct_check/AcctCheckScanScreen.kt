package com.etag.stsyn.ui.screen.acct_check

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.R
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.FilterDialog
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.states.rememberMutableDialogState
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.ui.theme.errorColor
import com.etag.stsyn.util.datasource.DataSource
import com.tzh.retrofit_module.domain.model.FilterItem
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AcctCheckScanScreen(
    accountCheckViewModel: AccountCheckViewModel,
    modifier: Modifier = Modifier
) {
    val TAG = "AccountCheckScanScreen"

    var filterCount by remember { mutableStateOf(0) }
    var filters by remember { mutableStateOf<List<FilterItem>>(emptyList()) }
    var showFilterDialog by remember { mutableStateOf(false) }
    val rfidUiState by accountCheckViewModel.rfidUiState.collectAsStateWithLifecycle()
    val acctCheckUiState by accountCheckViewModel.acctCheckUiState.collectAsStateWithLifecycle()
    val scannedItemIdList by accountCheckViewModel.scannedItemIdList.collectAsStateWithLifecycle()
    var total by remember { mutableStateOf(0) }
    var done by remember { mutableStateOf(0) }
    var outstanding by remember { mutableStateOf(0) }
    val dialogState = rememberMutableDialogState(data = "")

    LaunchedEffect(Unit) {
        accountCheckViewModel.hideUnknownEpcDialog()
    }

    LaunchedEffect(scannedItemIdList, acctCheckUiState.allItems) {
        withContext(Dispatchers.IO) {
            total = acctCheckUiState.allItems.size
            done = acctCheckUiState.allItems.filter { it.epc in scannedItemIdList }.size
            outstanding = total - done
        }
    }

    LaunchedEffect(acctCheckUiState.unknownEpc) {
        if (acctCheckUiState.unknownEpc != null) dialogState.showDialog("Alien/Unknown EPC alert. Scan '${acctCheckUiState.unknownEpc}' again?")
        else dialogState.hideDialog()
    }

    WarningDialog(
        icon = CustomIcon.Resource(R.drawable.warning_dialog),
        color = errorColor,
        dialogState = dialogState,
        positiveButtonTitle = "Yes",
        negativeButtonTitle = "No",
        onPositiveButtonClick = {
            //TODO do something here
        },
        onNegativeButtonClick = {
            //TODO do something here
        }
    )

    LaunchedEffect(acctCheckUiState.filterOptions) {
        filters = acctCheckUiState.filterOptions
        filterCount = acctCheckUiState.filterOptions.filter { it.selectedOption.isNotEmpty() }.size
    }

    Column(
        modifier = modifier.animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .animateContentSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            FilterDialog(
                show = showFilterDialog,
                filters = filters,
                onDismiss = { showFilterDialog = false },
                onClear = accountCheckViewModel::clearFilters,
                onDone = { filterItems ->
                    accountCheckViewModel.updateFilterOptions(filterItems)
                }
            )
            AcctCheckContent(
                onFilterButtonClick = { showFilterDialog = true },
                selectedFilters = acctCheckUiState.filterOptions,
                total = total,
                done = done,
                outstanding = outstanding,
                filterCount = filterCount,
                boxItem = acctCheckUiState.scannedItem,
                onReset = accountCheckViewModel::resetScannedItems
            )
        }
        ScanIconButton(
            isScanning = rfidUiState.isScanning,
            onScan = accountCheckViewModel::toggle,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}

@Composable
private fun AcctCheckContent(
    filterCount: Int,
    total: Int,
    done: Int,
    boxItem: BoxItem,
    outstanding: Int,
    onFilterButtonClick: () -> Unit,
    onReset: () -> Unit,
    selectedFilters: List<FilterItem>
) {
    FilterButton(filterCount, onClick = onFilterButtonClick)
    Spacer(modifier = Modifier.height(16.dp))
    selectedFilters.forEach {
        DetailItem(
            title = it.title,
            value = it.selectedOption
        )
    }
    ScanBoxSection(boxItem.id ?: "", boxItem.description ?: "")
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            DetailItem(title = "Total", value = total.toString())
            DetailItem(title = "Done", value = done.toString())
            DetailItem(title = "Outstanding", value = outstanding.toString())
        }
        TextButton(onClick = onReset, modifier = Modifier) {
            Text(
                text = "Reset",
                color = if (done > 0) Purple80 else Color.Gray,
                modifier = Modifier,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
private fun ScanBoxSection(
    description: String,
    id: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(
                color = Purple80.copy(0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Last Scan",
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            fontStyle = FontStyle.Italic
        )
        Text("Item Description:- ${description.uppercase()}")
        Text("ID:- ${id.uppercase()}")
    }
}

@Composable
private fun DetailItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier.padding(vertical = 4.dp)
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title, modifier = Modifier.weight(0.4f))
        Text(text = if (value.isEmpty()) "-" else value, modifier = Modifier.weight(0.6f))
    }
}

@Composable
private fun FilterButton(filterCount: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Purple80.copy(0.1f), shape = RoundedCornerShape(3.dp))
            .clickable { onClick() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Filter applied ($filterCount/8)", fontWeight = FontWeight.Bold)
        Icon(imageVector = Icons.Default.FilterAlt, tint = Purple80, contentDescription = null)
    }
}
