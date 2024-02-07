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
import com.etag.stsyn.ui.components.FilterDialog
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.util.datasource.DataSource
import com.tzh.retrofit_module.domain.model.FilterItem

@Composable
fun AcctCheckScanScreen(
    accountCheckViewModel: AccountCheckViewModel,
    modifier: Modifier = Modifier
) {
    val TAG = "AccountCheckScanScreen"

    var filterCount by remember { mutableStateOf(0) }
    var isScanned by remember { mutableStateOf(false) }
    var filters by remember { mutableStateOf<List<FilterItem>>(emptyList()) }
    var showFilterDialog by remember { mutableStateOf(false) }
    val rfidUiState by accountCheckViewModel.rfidUiState.collectAsState()
    val acctCheckUiState by accountCheckViewModel.acctCheckUiState.collectAsState()

    LaunchedEffect(acctCheckUiState.filterOptions) {
        filters = acctCheckUiState.filterOptions
        Log.d(TAG, "AcctCheckScanScreen: ${acctCheckUiState.filterOptions.map { it.selectedOption }}")
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
                onClear = {accountCheckViewModel.clearFilters()},
                onDone = {
                    accountCheckViewModel.updateFilterOptions(it)
                    filterCount = it.filter { it.selectedOption != "-" }.size
                }
            )
            AcctCheckContent(
                onFilterButtonClick = { showFilterDialog = true },
                selectedFilters = acctCheckUiState.filterOptions,
                filterCount = filterCount,
                isScanned = isScanned
            )
        }
        ScanIconButton(
            isScanning = rfidUiState.isScanning,
            onScan = { accountCheckViewModel.toggle() },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}

@Composable
private fun AcctCheckContent(
    filterCount: Int,
    onFilterButtonClick: () -> Unit,
    selectedFilters: List<FilterItem>,
    isScanned: Boolean
) {
    FilterButton(filterCount, onClick = onFilterButtonClick)
    Spacer(modifier = Modifier.height(16.dp))
    selectedFilters.forEach {
        DetailItem(
            title = it.title,
            value = it.selectedOption
        )
    }
    ScanBoxSection("", "")
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(0.8f)) {
            DetailItem(title = "Total", value = "0")
            DetailItem(title = "Done", value = "0")
            DetailItem(title = "Outstanding", value = "0")
        }
        TextButton(onClick = { }, modifier = Modifier.weight(0.2f)) {
            Text(
                text = "Reset",
                color = if (isScanned) Purple80 else Color.Gray,
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
        Text(text = title, modifier = Modifier.weight(0.3f))
        Text(text = if (value.isEmpty()) "-" else value, modifier = Modifier.weight(0.7f))
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
