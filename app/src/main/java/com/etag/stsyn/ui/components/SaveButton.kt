package com.etag.stsyn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Text(
        text = "Save",
        color = Color.White,
        modifier = modifier
            .background(
                color = Purple80,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SaveButtonPreview() {
    SaveButton {}
}