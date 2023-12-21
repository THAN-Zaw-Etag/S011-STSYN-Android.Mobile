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
fun UserItemLayout(
    profile: ImageVector,
    profileType: String,
    showRefreshIcon: Boolean = false,
    userName: String,
    userId: String,
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Icon(imageVector = profile, contentDescription = null, tint = Purple80)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = profileType
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "$userName - $userId")
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
                .background(Color.Gray)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun UserItemLayoutPreview() {
    UserItemLayout(
        showRefreshIcon = true,
        profile = Icons.Default.PersonPinCircle,
        profileType = "User",
        userName = "Admin",
        userId = "123s",
        onRefresh = { }
    )
}