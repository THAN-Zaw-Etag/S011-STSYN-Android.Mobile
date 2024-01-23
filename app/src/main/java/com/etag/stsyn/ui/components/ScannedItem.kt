@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class
)

package com.etag.stsyn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.theme.DarkGreen
import com.etag.stsyn.ui.theme.LightGreen
import com.etag.stsyn.ui.theme.errorColor
import com.kevinnzou.compose.swipebox.SwipeBox
import com.kevinnzou.compose.swipebox.SwipeDirection
import com.kevinnzou.compose.swipebox.widget.SwipeIcon
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()
    if (isSwipeable) {
        SwipeBox(
            modifier = Modifier.fillMaxWidth(),
            swipeDirection = SwipeDirection.EndToStart,
            endContentWidth = 60.dp,
            endContent = { swipeableState, endSwipeProgress ->
                SwipeIcon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    background = errorColor,
                    tint = Color.White
                ) {
                    onSwipeToDismiss()
                    coroutineScope.launch { swipeableState.animateTo(0) }
                }
            }
        ) { _, _, _ ->
            ScannedItemContent(
                isScanned = isScanned,
                id = id,
                showError = showError,
                showTrailingIcon = showTrailingIcon,
                onItemClick = onItemClick,
                name = name,
                modifier = modifier
            )
        }
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
private fun ScannedItemContent(
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
                modifier = modifier.padding(16.dp)
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
        }
    }
}

/*@Composable
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
}*/

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ScannedItemPreview() {
    ScannedItem(
        onSwipeToDismiss = {},
        isScanned = false,
        isSwipeable = false,
        id = "SN000001 - DLJC11111",
        name = "DATA LINK JUMPER CABLE",
        showTrailingIcon = true
    )
}