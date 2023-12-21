@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.theme.DarkGreen
import com.etag.stsyn.ui.theme.LightGreen

@Composable
fun ScannedItem(
    id: String,
    name: String,
    isScanned: Boolean = false,
    isSwipeable: Boolean = false,
    onSwipeToDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    val state = rememberDismissState(
        confirmValueChange = {
            if (isSwipeable && it == DismissValue.DismissedToStart) {
                onSwipeToDismiss()
            }
            true
        }
    )

    if (isSwipeable && !isScanned) {
        SwipeToDismiss(
            state = state,
            modifier = modifier,
            background = {
                ScannedItemDismissBackground()
            },
            dismissContent = {
                ScannedItemContent(id = id, name = name, isScanned = isScanned)
            }
        )
    } else ScannedItemContent(isScanned = isScanned, id = id, name = name, modifier = modifier)
}

@Composable
fun ScannedItemContent(
    id: String,
    name: String,
    isScanned: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .background(Color.White)
            .border(
                width = 1.dp,
                color = if (isScanned) DarkGreen else Color.LightGray,
                shape = RoundedCornerShape(3.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isScanned) LightGreen else Color.Transparent
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
private fun ScannedItemDismissBackground(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.error)
    ) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            tint = Color.White,
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterEnd)
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ScannedItemPreview() {
    ScannedItem(
        onSwipeToDismiss = {},
        id = "SN000001 - DLJC11111",
        name = "DATA LINK JUMPER CABLE"
    )
}