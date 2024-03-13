package com.etag.stsyn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tzh.retrofit_module.domain.model.ExpandedScannedItemModel

@Composable
fun ExpandedScannedItem(
    bookInItem: ExpandedScannedItemModel? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp, Color.LightGray, RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = "${bookInItem?.serialNo}".uppercase())
            Text(text = "${bookInItem?.code}".uppercase())
        }
        Text(text = "${bookInItem?.description}".uppercase())
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DetailItem(title = "Location", value = "${bookInItem?.location}")
            DetailItem(title = "Store Location", value = "${bookInItem?.storeLocation}")
            DetailItem(title = "Status", value = "${bookInItem?.status}")
        }
    }
}

@Composable
private fun DetailItem(
    title: String,
    value: String
) {
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, color = Color.Gray)
        Text(text = value.uppercase())
    }
}