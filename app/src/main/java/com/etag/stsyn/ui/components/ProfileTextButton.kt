package com.etag.stsyn.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun ProfileTextButton(
    text: String,
    onOptionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Purple80,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier.clickable { onOptionClick() }
    )
}

@Composable
@Preview(showBackground = true)
fun ProfileTextButtonPreview() {
    ProfileTextButton(text = "Hello", onOptionClick = { /*TODO*/ })
}