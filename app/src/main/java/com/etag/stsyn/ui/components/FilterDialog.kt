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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.etag.stsyn.ui.screen.acct_check.FilterItem

@Composable
fun FilterDialog(
    filters: List<FilterItem>,
    show: Boolean, onDismiss: () -> Unit,
    onDone: (HashMap<Int, String>) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(show) {
        showDialog = show
    }

    if (showDialog) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {
                showDialog = false
                onDismiss()
            }) {
            FilterDialogContent(
                onDismiss = {
                    showDialog = false
                    onDismiss()
                },
                filters = filters,
                onDone = {
                    onDone(it)
                    showDialog = false
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun FilterDialogContent(
    filters: List<FilterItem>,
    onDismiss: () -> Unit,
    onDone: (HashMap<Int, String>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        var selectedItems = hashMapOf<Int, String>()
        var isClear by remember {
            mutableStateOf(false)
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
            itemsIndexed(filters.toList()) { index, item ->
                FilterItemLayout(title = item.title, isClear = isClear, onSelected = { option ->
                    selectedItems.put(index, option)
                })
            }
        }

        Column {
            Button(
                onClick = {
                    selectedItems.clear()
                    isClear = true
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
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
) {

    var defaultValue by remember {
        mutableStateOf("All")
    }

    LaunchedEffect(isClear) {
        Log.d("TAG", "FilterItemLayout: $isClear")
        if (isClear) defaultValue = "All"
    }

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$title :", modifier = Modifier.weight(0.4f))
        Spacer(modifier = Modifier.width(8.dp))
        DropDown(
            modifier = Modifier.weight(0.6f),
            items = listOf("One", "Two", "Three"),
            defaultValue = defaultValue,
            onSelected = {
                defaultValue = it
                onSelected(it)
            }
        )
    }
}
