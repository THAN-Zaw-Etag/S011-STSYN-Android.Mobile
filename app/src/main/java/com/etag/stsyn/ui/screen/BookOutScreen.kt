package com.etag.stsyn.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.OptionButtonLayout
import com.etag.stsyn.util.DataSource

@Composable
fun BookOutScreen(
    onOptionButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DataSource.bookOutOptions.forEach {
            OptionButtonLayout(optionButtonModel = it, onOptionButtonClick = onOptionButtonClick)
        }
    }
}
