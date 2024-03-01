package com.etag.stsyn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPinCircle
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun SaveItemLayout(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    itemTitle: String,
    showRefreshIcon: Boolean = false,
    onRefresh: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Purple80)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = itemTitle
                )
                Spacer(modifier = Modifier.height(12.dp))
                content()
            }
            Spacer(modifier = Modifier.weight(1f))
            if (showRefreshIcon) {
                IconButton(onClick = onRefresh) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        tint = Color.Gray,
                        contentDescription = null
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun UserItemLayoutPreview() {
    SaveItemLayout(
        showRefreshIcon = true,
        icon = Icons.Default.PersonPinCircle,
        itemTitle = "User",
        onRefresh = { }
    ) {

    }
}