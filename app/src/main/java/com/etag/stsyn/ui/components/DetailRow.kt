package com.etag.stsyn.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.etag.stsyn.domain.model.ItemDetail

@Composable
fun DetailRow(
    itemDetail: ItemDetail,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(itemDetail.title)
        Text(itemDetail.value)
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DetailRowPreview() {
    DetailRow(itemDetail = ItemDetail("Serial No.", "SN001001"))
}