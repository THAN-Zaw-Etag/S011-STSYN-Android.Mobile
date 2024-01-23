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
            DetailItem(title = "Serial No.", value = boxItem.serialNo)
            DetailItem(title = "Location", value = boxItem.itemLocation)
            DetailItem(title = "Item Type", value = boxItem.itemType)
            DetailItem(title = "Description", value = boxItem.description)
            DetailItem(title = "Title", value = boxItem.title)
            DetailItem(title = "Unit Sqn", value = boxItem.unit)
            DetailItem(title = "Flight", value = boxItem.flight)
            DetailItem(title = "Store Type", value = boxItem.storeType)
            DetailItem(title = "CTK", value = boxItem.ctk)
            DetailItem(title = "Document No.", value = boxItem.docNo)
            DetailItem(title = "Remarks", value = boxItem.remarks)
            DetailItem(title = "Part Pub No.", value = boxItem.partNo)
            DetailItem(title = "Work Location", value = boxItem.workLocation)
            DetailItem(title = "EPC", value = boxItem.epc)
            DetailItem(title = "Item Status", value = boxItem.itemStatus)
        }
    }
}

@Composable
private fun DetailItem(
    title: String?,
    value: String?
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = title ?: "-", color = Color.DarkGray)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = value ?: "-", textAlign = TextAlign.End)
    }
}