@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun AppBar(
    title: String,
    onDrawerIconClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = title, modifier = Modifier.padding(horizontal = 8.dp)) },
        navigationIcon = {
            IconButton(onClick = onDrawerIconClick) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
            }
        },
        actions = {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Purple80)
            ) {
                Text(
                    text = "A",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    )
}

@Composable
@Preview(showSystemUi = true)
fun AppBarPreview() {
    AppBar(title = "Home", onDrawerIconClick = {})
}