package com.etag.stsyn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun ScannedBoxItem(
    boxTitle: String,
    boxDescription: String,
    showRefreshIcon: Boolean = false,
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Purple80.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = boxTitle,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = boxDescription.toUpperCase()
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        if (showRefreshIcon) {
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    tint = Color.Gray,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun ScannedBoxItemPreview() {
    ScannedBoxItem(
        boxTitle = "Box",
        boxDescription = "C1243533"
    )
}