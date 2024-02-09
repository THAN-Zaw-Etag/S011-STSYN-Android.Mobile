@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.etag.stsyn.R
import com.etag.stsyn.ui.states.rememberMutableDialogState
import com.tzh.retrofit_module.domain.model.FilterItem

@Composable
fun FilterDialog(
    filters: List<FilterItem>,
    show: Boolean,
    onClear: () -> Unit,
    onDismiss: () -> Unit,
    onDone: (List<FilterItem>) -> Unit
) {
    val TAG = "FilterDialog"
    var showFilterDialog by remember { mutableStateOf(false) }
    var filterItems by remember { mutableStateOf<MutableList<FilterItem>>(mutableListOf()) }
    val dialogState = rememberMutableDialogState(data = "")

    LaunchedEffect(show) {
        showFilterDialog = show
    }

    WarningDialog(
        icon = CustomIcon.Resource(R.drawable.warning_dialog),
        dialogState = dialogState,
        positiveButtonTitle = "Ok",
        onPositiveButtonClick = {
            onDone(filterItems)

            showFilterDialog = false
            onDismiss()
        }
    )

    if (showFilterDialog) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {
                showFilterDialog = false
                onDismiss()
            }) {
            FilterDialogContent(
                onDismiss = {
                    showFilterDialog = false
                    onDismiss()
                },
                filters = filters,
                onClear = onClear,
                onDone = { items ->
                    dialogState.showDialog(
                        data = if (items.filter { it.selectedOption.isEmpty() || it.selectedOption == "All" }.size != items.size) "You're not able to scan unknown EPC" else "You're able to scan unknown EPC"
                    )

                    filterItems.clear()
                    filterItems.addAll(items)

                }
            )
        }
    }
}

@Composable
private fun FilterDialogContent(
    filters: List<FilterItem>,
    onDismiss: () -> Unit,
    onClear: () -> Unit,
    onDone: (List<FilterItem>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        var selectedItems = mutableListOf<FilterItem>()
        var isClear by remember { mutableStateOf(false) }
        var defaultValue by remember { mutableStateOf("All") }

        LaunchedEffect(filters) {
            selectedItems.clear()
            selectedItems.addAll(filters)
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            IconButton(onClick = onDismiss) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Filter")
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(filters) { index, item ->
                defaultValue = if (item.selectedOption.trim().isEmpty()) "All" else item.selectedOption
                key (item.selectedOption) {
                    FilterItemLayout(
                        title = item.title,
                        isClear = isClear,
                        defaultValue = defaultValue,
                        options = filters[index].options,
                        onCleared = { isClear = false },
                        onSelected = { option ->
                            val updatedItem = item.copy(selectedOption = if (option == "All") "" else option)
                            selectedItems[index] = updatedItem
                        }
                    )
                }
            }
        }

        Column {
            Button(
                onClick = {

                    val temp = mutableListOf<FilterItem>()
                    temp.addAll(selectedItems)
                    selectedItems.clear()
                    selectedItems.addAll(temp.map { it.copy(selectedOption = "") })

                    isClear = true
                    onClear()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        color = MaterialTheme.colorScheme.primary,
                        width = 1.dp,
                        shape = RoundedCornerShape(24.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                Text(text = "Delete Filter")
            }
            Spacer(modifier = Modifier.height(8.dp))
            FilledTonalButton(
                onClick = { onDone(selectedItems) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
private fun FilterItemLayout(
    title: String,
    isClear: Boolean,
    defaultValue: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    onCleared: () -> Unit,
    modifier: Modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
) {

    var defaultValue by remember {
        mutableStateOf(defaultValue)
    }

    LaunchedEffect(isClear) {
        if (isClear) {
            defaultValue = options.get(0)
            onCleared()
        }
    }

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$title :", modifier = Modifier.weight(0.4f))
        Spacer(modifier = Modifier.width(8.dp))
        DropDown(
            modifier = Modifier.weight(0.6f),
            items = options,
            defaultValue = defaultValue,
            onSelected = {
                defaultValue = it
                onSelected(it)
            }
        )
    }
}
