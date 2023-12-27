package com.etag.stsyn.ui.screen.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.InfoBottomSheetContent

@Composable
fun BoxDetailScreen(
    title: String,
    modifier: Modifier = Modifier,
) {
    InfoBottomSheetContent(title = title, modifier = modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(10) {
                DetailItem(title = "Serial No.", value = "c123456")
            }
        }
    }
}

@Composable
private fun DetailItem(
    title: String,
    value: String
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title, color = Color.DarkGray)
        Text(text = value.uppercase(), fontWeight = FontWeight.Bold)
    }
}