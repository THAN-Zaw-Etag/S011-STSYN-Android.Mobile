package com.etag.stsyn.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DismissDirection
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.etag.stsyn.domain.model.DetPLoanItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetPLoanSwipeableItem(
    isSwipeable: Boolean,
    item: DetPLoanItem,
    onItemClick: () -> Unit,
    onSwipeToDismiss: (DetPLoanItem) -> Unit
) {

    var swipeable by remember { mutableStateOf(false) }

    LaunchedEffect(swipeable) {
        swipeable = isSwipeable
    }

    val state = rememberDismissState(initialValue = DismissValue.Default, confirmStateChange = {
        if (it == DismissValue.DismissedToStart) {
            onSwipeToDismiss(item)
        }
        true
    })

    val isDismissed = state.isDismissed(DismissDirection.EndToStart)

    if (swipeable) {
        // the item will only be showed if the item is not swiped or it will be hidden
        AnimatedVisibility(visible = !isDismissed) {
            SwipeableItem(state = state) {
                DetPLoanItemContent(item, onItemClick = onItemClick)
            }
        }
    } else {
        DetPLoanItemContent(item, onItemClick = onItemClick)
    }
}

@Composable
private fun DetPLoanItemContent(
    item: DetPLoanItem,
    onItemClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(
                1.dp, Color.LightGray, RoundedCornerShape(5.dp)
            )
            .clickable { onItemClick() }
            .padding(16.dp)
    ) {
        Text(text = item.id.uppercase())
        Text(text = item.name.uppercase())
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.type.uppercase())
            Text(text = item.code, fontSize = 12.sp)
        }
    }
}