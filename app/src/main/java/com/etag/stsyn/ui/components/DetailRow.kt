package com.etag.stsyn.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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