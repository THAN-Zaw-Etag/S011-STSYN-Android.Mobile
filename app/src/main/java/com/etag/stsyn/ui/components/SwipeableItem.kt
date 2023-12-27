@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.components

import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SwipeableItem(
    state: DismissState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SwipeToDismiss(
        state = state,
        directions = setOf(DismissDirection.EndToStart),
        modifier = modifier,
        background = { ScannedItemDismissBackground() },
        dismissContent = { content() })
}