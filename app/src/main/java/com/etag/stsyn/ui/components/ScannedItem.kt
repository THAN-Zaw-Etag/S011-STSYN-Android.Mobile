package com.etag.stsyn.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScannedItem(
    id: String,
    name: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier.border(
            width = 1.dp,
            color = Color.Gray,
            shape = RoundedCornerShape(3.dp)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = id)
            Text(text = name)
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ScannedItemPreview() {
    ScannedItem(
        id = "SN000001 - DLJC11111",
        name = "DATA LINK JUMPER CABLE"
    )
}