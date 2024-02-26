package com.etag.stsyn.ui.screen.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.InfoBottomSheetContent
import com.etag.stsyn.ui.theme.Purple80
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem

@Composable
fun BoxListBottomSheetScreen(
    boxItems: List<BoxItem>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    InfoBottomSheetContent(title = "List of booked out (Box)") {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(boxItems) { index, item ->
                BookOutBoxItem(onClick = { onItemClick(index) }, boxItem = item)
            }
        }
    }
}

@Composable
private fun BookOutBoxItem(
    boxItem: BoxItem,
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(color = Purple80.copy(0.1f), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = boxItem.serialNo.uppercase(), color = Purple80)
            IconButton(onClick = { onClick() }) {
                Icon(
                    imageVector = Icons.Default.Storage, tint = Purple80, contentDescription = null
                )
            }
        }
        Text(text = boxItem.description.uppercase(), color = Purple80)
    }
}