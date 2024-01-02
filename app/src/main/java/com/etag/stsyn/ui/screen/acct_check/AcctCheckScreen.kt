package com.etag.stsyn.ui.screen.acct_check

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.etag.stsyn.LocalRfidViewModel
import com.etag.stsyn.ui.components.FilterDialog
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.util.DataSource

@Composable
fun AcctCheckScreen(
    accountCheckViewModel: AccountCheckViewModel,
    modifier: Modifier = Modifier
) {
    var filterCount by remember { mutableStateOf(0) }
    var isScanned by remember { mutableStateOf(false) }
    var selectedFilters = remember { mutableStateOf(mutableMapOf<Int, String>()) }
    var showFilterDialog by remember { mutableStateOf(false) }
    val acctCheckUiState by accountCheckViewModel.acctCheckUiState.collectAsState()
    val rfidViewModel = LocalRfidViewModel.current
    val rfidUiState by rfidViewModel.rfidUiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(rfidUiState.isScanned) {
        if (rfidUiState.isScanned) Toast.makeText(
            context,
            "Scanned successfully!",
            Toast.LENGTH_LONG
        ).show()
    }

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            FilterDialog(showFilterDialog, onDismiss = { showFilterDialog = false }, onDone = {
                selectedFilters.value = it
                Log.d("TAG", "AcctCheckScreen: ${selectedFilters.value}")
            })
            AcctCheckContent(
                onFilterButtonClick = { showFilterDialog = true },
                selectedFilters = selectedFilters.value,
                filterCount = selectedFilters.value.size,
                isScanned = isScanned
            )
        }
        ScanIconButton(
            onScan = { rfidViewModel.startScan() },
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
    selectedFilters: MutableMap<Int, String>,
    isScanned: Boolean
) {
    FilterButton(filterCount, onClick = onFilterButtonClick)
    Spacer(modifier = Modifier.height(16.dp))
    DataSource.filters.forEachIndexed { index, title ->
        DetailItem(
            title = title,
            value = if (index in selectedFilters.keys) selectedFilters.getValue(index) else "-"
        )
    }
    ScanBoxSection("", "")
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(0.8f)) {
            DetailItem(title = "Total", value = "0")
            DetailItem(title = "Done", value = "0")
            DetailItem(title = "Outstanding", value = "0")
        }
        Text(
            text = "Reset",
            color = if (isScanned) Purple80 else Color.Gray,
            modifier = Modifier
                .weight(0.2f)
                .clickable { },
            textDecoration = TextDecoration.Underline
        )
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
        Text("Item Description:- ${description.toUpperCase()}")
        Text("ID:- ${id.toUpperCase()}")
    }
}

@Composable
private fun DetailItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier.padding(vertical = 4.dp)
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title, modifier = Modifier.weight(0.3f))
        Text(text = value, modifier = Modifier.weight(0.7f))
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
