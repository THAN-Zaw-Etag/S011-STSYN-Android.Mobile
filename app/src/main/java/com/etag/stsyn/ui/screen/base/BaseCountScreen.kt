package com.etag.stsyn.ui.screen.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ControlType
import com.etag.stsyn.ui.components.SegmentedControl

@Composable
fun BaseCountScreen(
    itemCount: Int,
    modifier: Modifier = Modifier,
    onTabSelected: (ControlType) -> Unit,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        SegmentedControl(onTabSelected = onTabSelected)
        Column(modifier = Modifier.weight(1f)) {
            content()
        }

        Text(
            text = "Total: $itemCount",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }

}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun BaseCountScreenPreview() {
    BaseCountScreen(4, onTabSelected = {}) {
        Column {

        }
    }
}