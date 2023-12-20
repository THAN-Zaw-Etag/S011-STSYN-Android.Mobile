package com.etag.stsyn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.theme.Purple80

enum class ControlType {
    All,
    Done,
    Outstanding
}

@Composable
fun SegmentedControl(
    onTabSelected: (ControlType) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf(ControlType.All) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Purple80)
            .border(
                1.dp,
                Purple80,
                RoundedCornerShape(3.dp)
            )
    ) {
        ControlType.values().forEach {
            Text(
                text = it.toString(),
                color = if (selected == it) Color.White else Purple80,
                modifier = Modifier
                    .weight(1f)
                    .background(if (selected == it) Purple80 else Color.White)
                    .padding(vertical = 8.dp)
                    .clickable {
                        selected = it
                        onTabSelected(it)
                    },
                textAlign = TextAlign.Center
            )
        }
    }
}