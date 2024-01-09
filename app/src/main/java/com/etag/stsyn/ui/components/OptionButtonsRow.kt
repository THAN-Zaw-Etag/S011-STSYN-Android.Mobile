package com.etag.stsyn.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.etag.stsyn.util.OptionButtonModel

@Composable
fun OptionsButtonRow(
    text: String,
    optionButtonModels: List<OptionButtonModel>,
    onOptionItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            optionButtonModels.forEach {
                OptionButtonLayout(
                    modifier = Modifier.weight(0.5f), // use layout weight 0.5f to get equal width items because it only has two items
                    optionButtonModel = it,
                    onOptionButtonClick = onOptionItemClick
                )
            }
        }
    }
}