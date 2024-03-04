@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.etag.stsyn.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.etag.stsyn.util.datasource.OptionButtonModel

@Composable
fun OptionButtonLayout(
    modifier: Modifier = Modifier,
    showButton: Boolean = true,
    optionButtonModel: OptionButtonModel,
    onOptionButtonClick: (String) -> Unit,
) {
    Card(
        enabled = showButton,
        onClick = { onOptionButtonClick(optionButtonModel.route) },
        modifier = modifier.border(
            width = 1.dp,
            color = Color.LightGray,
            shape = RoundedCornerShape(12.dp)
        ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Text(
            text = optionButtonModel.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}