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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TLoanScannedItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp, Color.LightGray, RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = "SN000001 - DLJC11111")
            Text(text = "000010001001")
        }
        Text(text = "DATA LINK JUMPER CABLE")
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DetailItem(title = "Location", value = "T001")
            DetailItem(title = "Store Location", value = "01-101")
            DetailItem(title = "Status", value = "out")
        }
    }
}

@Composable
private fun DetailItem(
    title: String,
    value: String
) {
    Column {
        Text(text = title, color = Color.Gray)
        Text(text = value.toUpperCase())
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun TLoanScannedItemPreview() {
    TLoanScannedItem()
}