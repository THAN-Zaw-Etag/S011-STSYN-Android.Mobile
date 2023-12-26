@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.etag.stsyn.util.DataSource

@Composable
fun FilterDialog(
    show: Boolean, onDismiss: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(show) {
        showDialog = show
        Log.d("TAG", "FilterDialog: $show")
    }

    if (showDialog) {
        Dialog(onDismissRequest = {
            showDialog = false
            onDismiss()
        }) {
            FilterDialogContent(onDismiss = {
                showDialog = false
                onDismiss()
            })
        }
    }
}

@Composable
private fun FilterDialogContent(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDismiss) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Filter")
        }

        LazyColumn {
            items(DataSource.filters) {
                FilterItem(title = it, onSelected = { option ->

                })
            }
            item {
                OutlinedButton(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    Text(text = "Delete Filter")
                }
                FilledTonalButton(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Save")
                }
            }
        }
    }
}

@Composable
fun FilterItem(
    title: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, modifier = Modifier.weight(0.4f))
        Spacer(modifier = Modifier.width(8.dp))
        DropDown(
            modifier = Modifier.weight(0.6f),
            items = listOf("One", "Two", "Three"),
            defaultValue = "All",
            onSelected = onSelected
        )
    }
}
