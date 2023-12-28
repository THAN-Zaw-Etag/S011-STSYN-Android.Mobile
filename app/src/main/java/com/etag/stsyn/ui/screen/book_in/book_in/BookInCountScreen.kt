package com.etag.stsyn.ui.screen.book_in.book_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ExpandedScannedItem
import com.etag.stsyn.ui.components.SegmentedControl

@Composable
fun BookInCountScreen(
    items: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SegmentedControl(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            onTabSelected = {})
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(10) {
                ExpandedScannedItem()
            }
        }

        Text(
            text = "Total ${items.size}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.End
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun BookInCountScreenPreview() {
    BookInCountScreen(listOf())
}