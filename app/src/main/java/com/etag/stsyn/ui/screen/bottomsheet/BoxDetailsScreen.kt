package com.etag.stsyn.ui.screen.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.InfoBottomSheetContent
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem

@Composable
fun BoxDetailScreen(
    boxItem: BoxItem,
    modifier: Modifier = Modifier,
) {
    InfoBottomSheetContent(title = boxItem.serialNo, modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            DetailItem(title = "Serial No.", description = boxItem.serialNo)
            DetailItem(title = "Location", description = boxItem.itemLocation)
            DetailItem(title = "Item Type", description = boxItem.itemType)
            DetailItem(title = "Description", description = boxItem.description)
            DetailItem(title = "Title", description = boxItem.title)
            DetailItem(title = "Unit Sqn", description = boxItem.unit)
            DetailItem(title = "Flight", description = boxItem.flight)
            DetailItem(title = "Store Type", description = boxItem.storeType)
            DetailItem(title = "CTK", description = boxItem.ctk)
            DetailItem(title = "Document No.", description = boxItem.docNo)
            DetailItem(title = "Remarks", description = boxItem.remarks)
            DetailItem(title = "Part Pub No.", description = boxItem.partNo)
            DetailItem(title = "Work Location", description = boxItem.workLocation)
            DetailItem(title = "EPC", description = boxItem.epc)
            DetailItem(title = "Item Status", description = boxItem.itemStatus)
        }
    }
}

@Composable
private fun DetailItem(
    title: String?,
    description: String?
) {
    var value by remember { mutableStateOf("") }
    LaunchedEffect(description) {
        value = description ?: ""
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = title ?: "-", color = Color.DarkGray)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = if (value.isEmpty()) "-" else value, textAlign = TextAlign.End)
    }
}