@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import com.etag.stsyn.ui.theme.errorColor

@Composable
fun ScannedItem(
    id: String,
    name: String,
    showTrailingIcon: Boolean = false,
    isScanned: Boolean = false,
    isSwipeable: Boolean = false,
    showError: Boolean = false,
    onItemClick: () -> Unit = {},
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
                ScannedItemContent(
                    id = id,
                    name = name,
                    showError = showError,
                    showTrailingIcon = showTrailingIcon,
                    onItemClick = onItemClick,
                    isScanned = isScanned
                )
            }
        )
    } else ScannedItemContent(
        isScanned = isScanned,
        id = id,
        showError = showError,
        showTrailingIcon = showTrailingIcon,
        onItemClick = onItemClick,
        name = name,
        modifier = modifier
    )
}

@Composable
fun ScannedItemContent(
    id: String,
    name: String,
    isScanned: Boolean,
    showError: Boolean,
    showTrailingIcon: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onItemClick,
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .background(Color.White)
            .border(
                width = 1.dp,
                color = if (isScanned) DarkGreen else Color.LightGray,
                shape = RoundedCornerShape(0.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isScanned) LightGreen else Color.Transparent
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = modifier
                    .padding(16.dp)
            ) {
                Text(text = id.toUpperCase())
                Text(text = name.toUpperCase())
            }

            Spacer(modifier = Modifier.weight(1f))

            if (showError) {
                Icon(
                    imageVector = Icons.Default.Info,
                    tint = errorColor,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentDescription = null,
                )
            }

            if (showTrailingIcon) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterVertically),
                    tint = Color.Gray
                )
            }

            //Text(text = "101100101", modifier = Modifier.align(Alignment.End))

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
        name = "DATA LINK JUMPER CABLE",
        showTrailingIcon = true
    )
}